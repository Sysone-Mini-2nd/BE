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

    String  memberRole = assignedPersonService.findRoleByProjectIdAndMemberId(projectId, memberId);

    if(memberRole.equals("USER")) {
        // 현재 유저에 할당된 이슈 데이터 가져옴
        List<Issue> issues = issueService.findByProjectIdAndMemberId(projectId,memberId);


        // 1. 특정 프로젝트 ID에 해당하는 이슈 내용 가져옴
        DashBoardProjectResponseDTO projectDto = findDashBoardProjectGraph(projectId,issues);

        // 2. 개발 속도 (이슈 체크)

        List<DashBoardUserIssueTrackingResponseDTO> issueTrackingDto = findDashBoardUserIssueTracking(issues);

        // 3. 앞으로 1주일 간 이슈리스트 출력
        DashBoardWeekendIssueResponseDTO weekendIssues = findDashBoardWeekendIssues(issues);

        // 4. 우선순위 작업
        DashBoardIssuePriorityResponseDTO priorityResponseDTO = findDashBoardPriority(issues);


        return DashBoardResponseDTO.builder()
                .role(memberRole)
                .projectGraph(projectDto)
                .issuesGraph(issueTrackingDto)
                .weekendIssues(weekendIssues)
                .priorities(priorityResponseDTO)
                .errorPriorities(null)
                .build();
    }else{
        // 현재 프로젝트에 할당된 이슈 데이터 가져옴
        List<Issue> issues = issueService.findByProjectId(projectId);


        // 1. 특정 프로젝트 ID에 해당하는 이슈 내용 가져옴
        DashBoardProjectResponseDTO projectDto = findDashBoardProjectGraph(projectId,issues);

        // 2. 개발 속도 (이슈 체크)
        List<DashBoardUserIssueTrackingResponseDTO> issueTrackingDto = findDashBoardIssueTrackingByMembers(projectId);


        // 3. 앞으로 1주일 간 이슈리스트 출력
        DashBoardWeekendIssueResponseDTO weekendIssues = findDashBoardWeekendIssues(issues);

        // 4. 우선순위 작업
        DashBoardIssuePriorityResponseDTO priorityResponseDTO = findDashBoardPriority(issues);

        // 5. 에러발생 작업
        List<DashBoardIssueErrorResponseDTO> errorResponseDTO = issues.stream()
                .filter(issue -> issue.getPriority().equals(IssuePriority.WARNING))
                .sorted(Comparator.comparing(Issue::getEndDate))  // endDate 기준 오름차순 정렬
                .map(issue -> DashBoardIssueErrorResponseDTO.builder()
                        .id(issue.getId())
                        .writerName(issue.getMemberId() == null ? "" : memberService.findMemberProfileById(issue.getMemberId()).getName())
                        .title(issue.getTitle())
                        .memberId(issue.getMemberId() == null ? 0L : issue.getMemberId())
                        .endDate(issue.getEndDate())
                        .build())
                .toList();


        return DashBoardResponseDTO.builder()
                .role(memberRole)
                .projectGraph(projectDto)
                .issuesGraph(issueTrackingDto)
                .weekendIssues(weekendIssues)
                .priorities(priorityResponseDTO)
                .errorPriorities(errorResponseDTO)
                .build();
        }

    }

    public  List<DashBoardProjectDataResponseDTO> findProjectData(ProjectListResponseDTO projectListResponse){

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



    public DashBoardProjectResponseDTO findDashBoardProjectGraph(Long projectId, List<Issue>  issues) {
        int todoCount = 0;           // 앞으로 할 일
        int progressCount = 0;      // 진행중
        int doneCount = 0;          // 완료
        int reviewCount = 0;        // 리뷰 중
        int totalCount = issues.size();

        for(Issue issue : issues) {
            if(issue.getStatus().equals(IssueStatus.DONE))
                doneCount++;
            if(issue.getStatus().equals(IssueStatus.IN_PROGRESS))
                progressCount++;
            if(issue.getStatus().equals(IssueStatus.REVIEW))
                reviewCount++;
            if(issue.getStatus().equals(IssueStatus.TODO))
                todoCount++;
        }

        log.info("todoCount :  " + todoCount);
        log.info("progressCOunt : " + progressCount);
        log.info("doneCount : " + doneCount);
        log.info("reviewCount : " + reviewCount);
        log.info("totalCount : " + totalCount);

        int todoCountResult = (int)(((double)todoCount / totalCount) * 100);
        int progressCountResult = (int)(((double)progressCount / totalCount) * 100);
        int doneCountResult = (int)(((double)doneCount / totalCount) * 100);
        int reviewCountResult = (int)(((double)reviewCount / totalCount) * 100);

        return DashBoardProjectResponseDTO.builder()
                .id(projectId)
                .total(totalCount)
                .progress(progressCountResult)
                .review(reviewCountResult)
                .done(doneCountResult)
                .todo(todoCountResult)
                .build();
    }


    public List<DashBoardUserIssueTrackingResponseDTO> findDashBoardUserIssueTracking(List<Issue>  issues) {
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


    // 프로젝트별 인원마다의 전체 이슈 기간 추출
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


    // 마감일까지 남은 일수 계산
    private int  calculateDaysUntilDeadline(Timestamp startDate, Timestamp deadLine) {
        if (startDate == null || deadLine == null) {
            return 0;
        }

        LocalDateTime start = startDate.toLocalDateTime();
        LocalDateTime end = deadLine.toLocalDateTime();

        return (int) ChronoUnit.DAYS.between(start, end);
    }

    // 총 작업 일수 계산
    private int calculateTotalDays(Timestamp startDate, Timestamp endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }

        LocalDateTime start = startDate.toLocalDateTime();
        LocalDateTime end = endDate.toLocalDateTime();

        return (int) ChronoUnit.DAYS.between(start, end);
    }


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
                .weekendIssue(resultMap)  // 이제 타입이 일치함
                .build();
    }

    private DashBoardIssuePriorityResponseDTO findDashBoardPriority(List<Issue> issues) {
        List<DashBoardIssuePriorityResponseDTO.PriorityData> lowPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> normalPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> highPriorityData = new ArrayList<>();
        List<DashBoardIssuePriorityResponseDTO.PriorityData> warningPriorityData = new ArrayList<>();

        // TODO member 연결되면 writerName 변경

        for(Issue issue : issues) {
            String writerName = issue.getMemberId() == null ? "" : memberService.findMemberProfileById(issue.getMemberId()).getName();

            if(issue.getPriority().equals(IssuePriority.LOW)) {
                    lowPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                            .id(issue.getId())
                            .title(issue.getTitle())
                            .writerName(writerName)
                            .build());

            }else if(issue.getPriority().equals(IssuePriority.NORMAL)){
                    normalPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                            .id(issue.getId())
                            .title(issue.getTitle())
                            .writerName(writerName)
                            .build());

            }else if(issue.getPriority().equals(IssuePriority.HIGH)){
                    highPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                            .id(issue.getId())
                            .title(issue.getTitle())
                            .writerName(writerName)
                            .build());

            }else if(issue.getPriority().equals(IssuePriority.WARNING)){
                    warningPriorityData.add(DashBoardIssuePriorityResponseDTO.PriorityData.builder()
                            .id(issue.getId())
                            .title(issue.getTitle())
                            .writerName(writerName)
                            .build());

            }
        }

        List<DashBoardIssuePriorityResponseDTO.PriorityList> list = new ArrayList<>();

        for(int i = 0; i <4; i++){
            if(i == 0){
                DashBoardIssuePriorityResponseDTO.PriorityList priorityList = DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                        .priority(IssuePriority.LOW)
                        .priorityDataList(lowPriorityData)
                        .build();

                list.add(priorityList);
            }else if(i == 1){
                DashBoardIssuePriorityResponseDTO.PriorityList priorityList = DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                        .priority(IssuePriority.NORMAL)
                        .priorityDataList(normalPriorityData)
                        .build();

                list.add(priorityList);
            }else if(i == 2){
                DashBoardIssuePriorityResponseDTO.PriorityList priorityList = DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                        .priority(IssuePriority.HIGH)
                        .priorityDataList(highPriorityData)
                        .build();

                list.add(priorityList);
            }else if(i == 3){
                DashBoardIssuePriorityResponseDTO.PriorityList priorityList = DashBoardIssuePriorityResponseDTO.PriorityList.builder()
                        .priority(IssuePriority.WARNING)
                        .priorityDataList(warningPriorityData)
                        .build();

                list.add(priorityList);
            }
        }
        return DashBoardIssuePriorityResponseDTO.builder().priority(list).build();
    }

}

