package com.sys.stm.domains.dashBoard.service;

import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDashBoardResponseDTO;
import com.sys.stm.domains.assignedPerson.service.AssignedPersonService;
import com.sys.stm.domains.dashBoard.dto.response.*;
import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.domain.IssuePriority;
import com.sys.stm.domains.issue.domain.IssueStatus;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.domains.issueLog.service.IssueLogService;
import com.sys.stm.domains.messenger.service.MemberProfileService;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;
import com.sys.stm.domains.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
/** 작성자: 배지원 */
@RequiredArgsConstructor
@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {
    private final MemberProfileService memberService;
    private final ProjectService projectService;
    private final IssueService issueService;
    private final IssueLogService issueLogService;
    private final AssignedPersonService assignedPersonService;

    @Override
    public DashBoardProjectListResponseDTO getProjectsByMemberId(Long memberId, String memberRole) {
        ProjectListResponseDTO projectListResponse;
        if(memberRole.equals("MASTER")){
            projectListResponse = projectService.getAllProject();
        }else{
            projectListResponse = projectService.getProjectsByMemberId(memberId);
        }

        List<DashBoardProjectDataResponseDTO> projects = findProjectData(projectListResponse);

        int projectCount = projects.size();

        // 프로젝트 ID 목록 추출
        List<Long> projectIds = projects.stream()
                .map(DashBoardProjectDataResponseDTO::getId)
                .toList();

        // 1주일 내 마감일이 있는 이슈 개수 조회
        int issueCount = issueService.countIssuesByProjectIdsAndEndDateWithinWeek(projectIds);

        return DashBoardProjectListResponseDTO.builder()
                .projectCount(projectCount)
                .issueCount(issueCount)
                .projects(projects)
                .build();
    }


    @Override
    public DashBoardResponseDTO findDashBoard(Long memberId, Long projectId) {
        String memberRole = assignedPersonService.findRoleByProjectIdAndMemberId(projectId, memberId);

        // 1. 역할별 이슈 데이터 조회 (유일한 차이점)
        List<Issue> issues = getIssuesByRole(memberId, projectId, memberRole);

        // 2. 공통 대시보드 데이터 생성 (재사용)
        DashBoardProjectResponseDTO projectGraph = createProjectGraph(projectId, issues);
        DashBoardWeekendIssueResponseDTO weekendIssues = createWeekendIssues(issues);
        DashBoardIssuePriorityResponseDTO priorities = createIssuePriorities(issues);

        // 3. 역할별 차별화된 데이터 생성
        List<DashBoardUserIssueTrackingResponseDTO> issueTracking = createIssueTracking(memberId, projectId, memberRole, issues);
        List<DashBoardIssueErrorResponseDTO> errorPriorities = createErrorPriorities(memberRole, issues);

        // 4. 통합 응답 객체 생성
        return buildDashBoardResponse(memberRole, projectGraph, issueTracking, weekendIssues, priorities, errorPriorities);
    }

    // 역할별 이슈 데이터 조회
    private List<Issue> getIssuesByRole(Long memberId, Long projectId, String memberRole) {
        if (memberRole != null && memberRole.equals("USER")) {
            // 일반 사용자: 자신에게 할당된 이슈만 조회
            return issueService.findByProjectIdAndMemberId(projectId, memberId);
        } else {
            // PM/MASTER: 프로젝트 전체 이슈 조회
            return issueService.findByProjectId(projectId);
        }
    }

    // 프로젝트 그래프 데이터 생성 - 모든 역할에서 공통 사용
    private DashBoardProjectResponseDTO createProjectGraph(Long projectId, List<Issue> issues) {
        return findDashBoardProjectGraph(projectId, issues);
    }

    // 주간 이슈 데이터 생성 - 모든 역할에서 공통 사용
    private DashBoardWeekendIssueResponseDTO createWeekendIssues(List<Issue> issues) {
        return findDashBoardWeekendIssues(issues);
    }

    // 이슈 우선순위 데이터 생성 - 모든 역할에서 공통 사용
    private DashBoardIssuePriorityResponseDTO createIssuePriorities(List<Issue> issues) {
        return findDashBoardPriority(issues);
    }

    // 이슈 추적 데이터 생성 - 역할별 차별화
    private List<DashBoardUserIssueTrackingResponseDTO> createIssueTracking(
            Long memberId, Long projectId, String memberRole, List<Issue> issues) {

        if (memberRole != null && memberRole.equals("USER")) {
            // 일반 사용자: 개인 이슈 추적 데이터
            return findDashBoardUserIssueTracking(issues);
        } else {
            // PM/MASTER: 팀 전체 이슈 추적 데이터
            return findDashBoardIssueTrackingByMembers(projectId);
        }
    }

    // 에러 우선순위 데이터 생성 - PM/MASTER만 제공
    private List<DashBoardIssueErrorResponseDTO> createErrorPriorities(String memberRole, List<Issue> issues) {
        if (memberRole != null && memberRole.equals("USER")) {
            // 일반 사용자: 에러 우선순위 미제공
            return null;
        } else {
            // PM/MASTER: WARNING 우선순위 이슈 제공
            return issues.stream()
                    .filter(issue -> issue.getPriority().equals(IssuePriority.WARNING))
                    .sorted(Comparator.comparing(Issue::getEndDate))
                    .map(issue -> DashBoardIssueErrorResponseDTO.builder()
                            .id(issue.getId())
                            .writerName(issue.getMemberId() == null ? "" :
                                    memberService.findMemberProfileById(issue.getMemberId()).getName())
                            .title(issue.getTitle())
                            .memberId(issue.getMemberId() == null ? 0L : issue.getMemberId())
                            .endDate(issue.getEndDate())
                            .build())
                    .toList();
        }
    }

    // 대시보드 응답 객체 생성 - 공통 로직
    private DashBoardResponseDTO buildDashBoardResponse(
            String memberRole,
            DashBoardProjectResponseDTO projectGraph,
            List<DashBoardUserIssueTrackingResponseDTO> issueTracking,
            DashBoardWeekendIssueResponseDTO weekendIssues,
            DashBoardIssuePriorityResponseDTO priorities,
            List<DashBoardIssueErrorResponseDTO> errorPriorities) {

        return DashBoardResponseDTO.builder()
                .role(memberRole == null ? "MASTER" : memberRole)
                .projectGraph(projectGraph)
                .issuesGraph(issueTracking)
                .weekendIssues(weekendIssues)
                .priorities(priorities)
                .errorPriorities(errorPriorities)
                .build();
    }

    // 프로젝트 데이터 변환 - 프로젝트 리스트 조회용
    public List<DashBoardProjectDataResponseDTO> findProjectData(ProjectListResponseDTO projectListResponse) {
        return projectListResponse.getProjects().stream()
                .map(project -> DashBoardProjectDataResponseDTO.builder()
                        .id(project.getId())
                        .name(project.getName())
                        .participant(assignedPersonService.findMembersNameByProjectId(project.getId()).stream()
                                .map(data -> data.getName())
                                .toList())
                        .progressRate(project.getProgressRate())
                        .totalTasks(project.getTotalTasks())
                        .endDate(project.getEndDate())
                        .build())
                .toList();
    }

    // 프로젝트 그래프 데이터 생성 - 이슈 상태별 통계 계산
    public DashBoardProjectResponseDTO findDashBoardProjectGraph(Long projectId, List<Issue> issues) {
        int todoCount = 0;           // 앞으로 할 일
        int progressCount = 0;      // 진행중
        int doneCount = 0;          // 완료
        int reviewCount = 0;        // 리뷰 중
        int totalCount = issues.size();

        // 이슈 상태별 카운팅
        for (Issue issue : issues) {
            if (issue.getStatus().equals(IssueStatus.DONE)) doneCount++;
            if (issue.getStatus().equals(IssueStatus.IN_PROGRESS)) progressCount++;
            if (issue.getStatus().equals(IssueStatus.REVIEW)) reviewCount++;
            if (issue.getStatus().equals(IssueStatus.TODO)) todoCount++;
        }

        log.info("todoCount: {}, progressCount: {}, doneCount: {}, reviewCount: {}, totalCount: {}",
                todoCount, progressCount, doneCount, reviewCount, totalCount);

        // 백분율 계산 (소수점 반올림)
        int todoCountResult = (int) (((double) todoCount / totalCount) * 100);
        int progressCountResult = (int) (((double) progressCount / totalCount) * 100);
        int doneCountResult = (int) (((double) doneCount / totalCount) * 100);
        int reviewCountResult = (int) (((double) reviewCount / totalCount) * 100);

        return DashBoardProjectResponseDTO.builder()
                .id(projectId)
                .total(totalCount)
                .progress(progressCountResult)
                .review(reviewCountResult)
                .done(doneCountResult)
                .todo(todoCountResult)
                .build();
    }

    // 사용자별 이슈 추적 데이터 생성 - 개인 이슈 추적용
    public List<DashBoardUserIssueTrackingResponseDTO> findDashBoardUserIssueTracking(List<Issue> issues) {
        return issues.stream()
                .filter(issue -> issue.getStatus().equals(IssueStatus.DONE))            // 작업이 끝난 데이터만 가져옴
                .map(issue ->
                        DashBoardUserIssueTrackingResponseDTO.builder()
                                .id(issue.getId())
                                .name(issue.getTitle())
                                .allottedPeriod(calculateDaysUntilDeadline(issue.getStartDate(), issue.getEndDate()))
                                .completedPeriod(calculateTotalDays(issue.getStartDate(), issueLogService.findByIssueId(issue.getId()).getEndDate()))
                                .build())
                .toList();
    }

    // 프로젝트별 인원마다의 전체 이슈 기간 추출 - 팀 이슈 추적용
    public List<DashBoardUserIssueTrackingResponseDTO> findDashBoardIssueTrackingByMembers(Long projectId) {
        // 1. 프로젝트에 참여하는 모든 멤버 조회
        List<AssignedPersonDashBoardResponseDTO> projectMembers = assignedPersonService.findMembersNameByProjectId(projectId);

        return projectMembers.stream()
                .map(member -> {
                    Long memberId = member.getId();
                    String memberName = member.getName();

                    // 2. 멤버의 완료된 이슈들 조회
                    List<Issue> memberIssues = issueService.findByProjectIdAndMemberId(projectId, memberId);

                    // 3. 기존 findDashBoardIssueTracking 로직 재사용
                    List<DashBoardUserIssueTrackingResponseDTO> memberIssueTrackings =
                            findDashBoardUserIssueTracking(memberIssues);

                    // 4. 멤버별 총합 계산
                    int totalAllottedPeriod = memberIssueTrackings.stream()
                            .mapToInt(DashBoardUserIssueTrackingResponseDTO::getAllottedPeriod)
                            .sum();

                    int totalCompletedPeriod = memberIssueTrackings.stream()
                            .mapToInt(DashBoardUserIssueTrackingResponseDTO::getCompletedPeriod)
                            .sum();

                    return DashBoardUserIssueTrackingResponseDTO.builder()
                            .id(memberId)
                            .name(memberName)
                            .allottedPeriod(totalAllottedPeriod)
                            .completedPeriod(totalCompletedPeriod)
                            .build();
                })
                .filter(tracking -> tracking.getAllottedPeriod() > 0) // 이슈가 있는 멤버만
                .collect(Collectors.toList());
    }

    // 마감일까지 남은 일수 계산 - 유틸리티 메서드
    private int calculateDaysUntilDeadline(Timestamp startDate, Timestamp deadLine) {
        if (startDate == null || deadLine == null) {
            return 0;
        }

        LocalDateTime start = startDate.toLocalDateTime();
        LocalDateTime end = deadLine.toLocalDateTime();

        return (int) ChronoUnit.DAYS.between(start, end);
    }

    // 총 작업 일수 계산 - 유틸리티 메서드
    private int calculateTotalDays(Timestamp startDate, Timestamp endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        LocalDateTime start = startDate.toLocalDateTime();
        LocalDateTime end = endDate.toLocalDateTime();

        return (int) ChronoUnit.DAYS.between(start, end);
    }

    // 주간 이슈 데이터 생성 - 요일별 그룹화
    private DashBoardWeekendIssueResponseDTO findDashBoardWeekendIssues(List<Issue> issues) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 주의 시작일(월요일)과 종료일(일요일) 계산
        LocalDateTime startOfWeek = now.with(java.time.DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfWeek = now.with(java.time.DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59);

        // 1주일 내 마감일이 있는 이슈들 필터링
        List<Issue> thisWeekIssues = issues.stream()
                .filter(issue -> issue.getEndDate() != null)
                .filter(issue -> {
                    LocalDateTime endDate = issue.getEndDate().toLocalDateTime();
                    return !endDate.isBefore(startOfWeek) && !endDate.isAfter(endOfWeek);
                })
                .sorted(Comparator.comparing(Issue::getEndDate))
                .collect(Collectors.toList());

        // 요일별로 이슈들을 그룹화
        Map<Integer, List<DashBoardWeekendIssueResponseDTO.WeekendIssueDTO>> weekendIssueMap =
                thisWeekIssues.stream()
                        .collect(Collectors.groupingBy(
                                issue -> issue.getEndDate().toLocalDateTime().getDayOfWeek().getValue(),
                                Collectors.mapping(
                                        issue -> DashBoardWeekendIssueResponseDTO.WeekendIssueDTO.builder()
                                                .id(issue.getId())
                                                .title(issue.getTitle())
                                                .date(issue.getEndDate())
                                                .status(issue.getStatus())
                                                .build(),
                                        Collectors.toList()
                                )
                        ));

        // 월요일부터 일요일까지 모든 요일을 포함하도록 보장
        Map<Integer, List<DashBoardWeekendIssueResponseDTO.WeekendIssueDTO>> resultMap = new HashMap<>();
        for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            resultMap.put(dayOfWeek, weekendIssueMap.getOrDefault(dayOfWeek, new ArrayList<>()));
        }

        return DashBoardWeekendIssueResponseDTO.builder()
                .weekendIssue(resultMap)
                .build();
    }

    // 이슈 우선순위별 데이터 생성 - 우선순위별 분류
    private DashBoardIssuePriorityResponseDTO findDashBoardPriority(List<Issue> issues) {
        List<DashBoardIssuePriorityResponseDTO.PriorityData> lowPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> normalPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> highPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> warningPriorityData = new ArrayList<>();

        // 이슈 우선순위별 분류
        for (Issue issue : issues) {
            String writerName = issue.getMemberId() == null ? "" :
                    memberService.findMemberProfileById(issue.getMemberId()).getName();

            if (issue.getPriority().equals(IssuePriority.LOW)) {
                lowPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .writerName(writerName)
                        .build());
            } else if (issue.getPriority().equals(IssuePriority.NORMAL)) {
                normalPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .writerName(writerName)
                        .build());
            } else if (issue.getPriority().equals(IssuePriority.HIGH)) {
                highPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .writerName(writerName)
                        .build());
            } else if (issue.getPriority().equals(IssuePriority.WARNING)) {
                warningPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .writerName(writerName)
                        .build());
            }
        }

        // 우선순위별 리스트 생성
        List<DashBoardIssuePriorityResponseDTO.PriorityList> list = new ArrayList<>();

        // LOW 우선순위
        list.add(DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                .priority(IssuePriority.LOW)
                .priorityDataList(lowPriorityData)
                .build());

        // NORMAL 우선순위
        list.add(DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                .priority(IssuePriority.NORMAL)
                .priorityDataList(normalPriorityData)
                .build());

        // HIGH 우선순위
        list.add(DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                .priority(IssuePriority.HIGH)
                .priorityDataList(highPriorityData)
                .build());

        // WARNING 우선순위
        list.add(DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                .priority(IssuePriority.WARNING)
                .priorityDataList(warningPriorityData)
                .build());

        return DashBoardIssuePriorityResponseDTO.builder()
                .priority(list)
                .build();
    }
}