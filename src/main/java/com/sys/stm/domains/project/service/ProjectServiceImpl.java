package com.sys.stm.domains.project.service;

import com.sys.stm.domains.assignedPerson.dao.AssignedPersonRepository;
import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import com.sys.stm.domains.assignedPerson.domain.AssignedPersonRole;
import com.sys.stm.domains.assignedPerson.dto.response.PmInfoResponseDTO;
import com.sys.stm.domains.issue.dao.IssueRepository;
import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.domain.IssuePriority;
import com.sys.stm.domains.issue.domain.IssueStatus;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.project.dao.ProjectRepository;
import com.sys.stm.domains.project.domain.Project;
import com.sys.stm.domains.project.domain.ProjectStatus;
import com.sys.stm.domains.project.dto.request.ProjectCreateRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectListRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectUpdateRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;
import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectStatsResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectSummaryResponseDTO;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final AssignedPersonRepository assignedPersonRepository;
    private final IssueRepository issueRepository;

    @Override
    public ProjectDetailResponseDTO getProject(Long projectId) {
        Project responseProject = projectRepository.findById(projectId);

        if(responseProject==null){
            throw new NotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }

        List<AssignedPersonDetailResponseDTO> members = assignedPersonRepository.findMembersByProjectId(projectId);

        String pmName = "";
        long pmId = 0L;
        for(AssignedPersonDetailResponseDTO member : members){
            AssignedPersonRole role = member.getRole();

            if(role.equals(AssignedPersonRole.PM)){
                pmName = member.getName();
                pmId = member.getId();
            }
        }

        return ProjectDetailResponseDTO.builder()
                .id(responseProject.getId())
                .name(responseProject.getName())
                .desc(responseProject.getDesc())
                .status(responseProject.getStatus())
                .priority(responseProject.getPriority())
                .totalMemberCount(
                        members.size())
                .startDate(responseProject.getStartDate())
                .endDate(responseProject.getEndDate())
                .pmId(pmId)
                .pmName(pmName)
                .members(members)
                .build();
    }

    @Override
    public ProjectListResponseDTO getProjectsByMemberId(Long memberId, ProjectListRequestDTO projectListRequestDTO) {
        List<Project> responseProjects = projectRepository.findAllByMemberId(memberId, projectListRequestDTO);

        if (responseProjects == null || responseProjects.isEmpty()) {
            return ProjectListResponseDTO.builder().build();
        }

        List<Long> projectIds = responseProjects.stream()
                .map(Project::getId)
                .toList();

        List<ProjectStatsResponseDTO> statsList = projectRepository.findProjectStatsByIds(projectIds);
        Map<Long, ProjectStatsResponseDTO> statsMap = statsList.stream()
                .collect(Collectors.toMap(ProjectStatsResponseDTO::getId, stats -> stats));

        List<PmInfoResponseDTO> pmInfoList = assignedPersonRepository.findPmsByProjectIds(projectIds);
        Map<Long, PmInfoResponseDTO> pmInfoMap = pmInfoList.stream()
                .collect(Collectors.toMap(PmInfoResponseDTO::getProjectId, pmInfo -> pmInfo));

        List<ProjectSummaryResponseDTO> dtoList = new ArrayList<>();
        Map<ProjectStatus, Integer> statusCountMap = new HashMap<>();
        statusCountMap.put(ProjectStatus.TODO, 0);
        statusCountMap.put(ProjectStatus.IN_PROGRESS, 0);
        statusCountMap.put(ProjectStatus.PAUSED, 0);
        statusCountMap.put(ProjectStatus.DONE, 0);

        int delayedCount = 0;
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        for (Project p : responseProjects) {
            ProjectStatsResponseDTO stats = statsMap.getOrDefault(p.getId(), new ProjectStatsResponseDTO());
            PmInfoResponseDTO pmInfo = pmInfoMap.get(p.getId());
            List<AssignedPersonDetailResponseDTO> members = assignedPersonRepository.findMembersByProjectId(p.getId());

            Long pmId = (pmInfo != null) ? pmInfo.getPmId() : null;
            String pmName = (pmInfo != null) ? pmInfo.getPmName() : "";

            statusCountMap.put(p.getStatus(), statusCountMap.getOrDefault(p.getStatus(), 0) + 1);

            if (p.getEndDate() != null && p.getStatus() != ProjectStatus.DONE && p.getEndDate().before(now)) {
                delayedCount++;
            }

            ProjectSummaryResponseDTO dto = ProjectSummaryResponseDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .desc(p.getDesc())
                    .status(p.getStatus())
                    .priority(p.getPriority())
                    .progressRate(stats.getProgressRate())
                    .completedTasks(stats.getCompletedTasks())
                    .totalTasks(stats.getTotalTasks())
                    .totalMemberCount(stats.getTotalMemberCount())
                    .startDate(p.getStartDate())
                    .endDate(p.getEndDate())
                    .pmName(pmName)
                    .pmId(pmId)
                    .members(members)
                    .build();

            dtoList.add(dto);
        }

        return ProjectListResponseDTO.builder()
                .projects(dtoList)
                .total(dtoList.size())
                .statusCounts(statusCountMap)
                .delayed(delayedCount)
                .build();
    }

    @Override
    public ProjectListResponseDTO getProjectsByMemberId(Long memberId) {
        List<Project> responseProjects = projectRepository.findAllByOnlyMemberId(memberId);

        if (responseProjects.isEmpty()) {
            return ProjectListResponseDTO.builder().build();
        }

        List<Long> projectIds = responseProjects.stream()
                .map(Project::getId)
                .toList();

        List<ProjectStatsResponseDTO> statsList = projectRepository.findProjectStatsByIds(projectIds);
        Map<Long, ProjectStatsResponseDTO> statsMap = statsList.stream()
                .collect(Collectors.toMap(ProjectStatsResponseDTO::getId, stats -> stats));

        List<PmInfoResponseDTO> pmInfoList = assignedPersonRepository.findPmsByProjectIds(projectIds);
        Map<Long, String> pmMap = pmInfoList.stream()
                .collect(Collectors.toMap(PmInfoResponseDTO::getProjectId, PmInfoResponseDTO::getPmName));

        List<ProjectSummaryResponseDTO> dtoList = new ArrayList<>();
        Map<ProjectStatus, Integer> statusCountMap = new HashMap<>();

        for (Project p : responseProjects) {
            ProjectStatsResponseDTO stats = statsMap.getOrDefault(p.getId(), new ProjectStatsResponseDTO());
            String pmName = pmMap.getOrDefault(p.getId(), "");

            statusCountMap.put(p.getStatus(), statusCountMap.getOrDefault(p.getStatus(), 0) + 1);

            ProjectSummaryResponseDTO dto = ProjectSummaryResponseDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .desc(p.getDesc())
                    .status(p.getStatus())
                    .progressRate(stats.getProgressRate())
                    .completedTasks(stats.getCompletedTasks())
                    .totalTasks(stats.getTotalTasks())
                    .totalMemberCount(stats.getTotalMemberCount())
                    .startDate(p.getStartDate())
                    .endDate(p.getEndDate())
                    .pmName(pmName)
                    .build();

            dtoList.add(dto);
        }

        return ProjectListResponseDTO.builder()
                .projects(dtoList)
                .total(dtoList.size())
                .statusCounts(statusCountMap)
                .build();
    }

    @Override
    @Transactional
    public ProjectDetailResponseDTO createProject(ProjectCreateRequestDTO projectCreateRequestDTO) {
        Project requestProject = Project.builder()
                .name(projectCreateRequestDTO.getName())
                .desc(projectCreateRequestDTO.getDesc())
                .status(projectCreateRequestDTO.getStatus())
                .priority(projectCreateRequestDTO.getPriority())
                .startDate(projectCreateRequestDTO.getStartDate())
                .endDate(projectCreateRequestDTO.getEndDate())
                .build();

        if(projectRepository.createProject(requestProject) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        createAssignedPersons(projectCreateRequestDTO.getMemberIds(), projectCreateRequestDTO.getPmId(), requestProject.getId());

        List<IssueCreateRequestDTO> dtoList = projectCreateRequestDTO.getIssues();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime endDate = now.plusDays(1);

        if(dtoList != null){
            for(IssueCreateRequestDTO dto : dtoList){
                issueRepository.createIssue(Issue.builder()
                        .projectId(requestProject.getId())
                        .title(dto.getTitle())
                        .desc(dto.getDesc())
                        .status(IssueStatus.TODO)
                        .priority(IssuePriority.NORMAL)
                        .startDate(Timestamp.valueOf(now))
                        .endDate(Timestamp.valueOf(endDate))
                        .build());
            }
        }

        return getProject(requestProject.getId());
    }

    @Override
    @Transactional
    public ProjectDetailResponseDTO updateProject(Long projectId, ProjectUpdateRequestDTO dto) {
        Project projectToUpdate = Project.builder()
                .id(projectId)
                .name(dto.getName())
                .desc(dto.getDesc())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
        if (projectRepository.updateProject(projectToUpdate) == 0) {
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        List<AssignedPersonDetailResponseDTO> currentMembers =
                assignedPersonRepository.findMembersByProjectId(
                projectId);
        long existingPmId = 0L;
        List<Long> existingUserIds = new ArrayList<>();
        for (AssignedPersonDetailResponseDTO member : currentMembers) {
            if (member.getRole().equals(AssignedPersonRole.PM)) {
                existingPmId = member.getId();
            } else {
                existingUserIds.add(member.getId());
            }
        }

        Long newPmId = dto.getPmId();
        List<Long> newUserIds = (dto.getMemberIds() != null) ? new ArrayList<>(dto.getMemberIds()) : Collections.emptyList();

        if (newPmId != null) {
            newUserIds.remove(newPmId);
        }

        if (newPmId != null && !newPmId.equals(existingPmId)) {
            if (existingPmId != 0L) {
                assignedPersonRepository.deleteByProjectIdAndMemberIds(projectId, List.of(existingPmId));
            }
            assignedPersonRepository.deleteByProjectIdAndMemberIds(projectId, List.of(newPmId));
            assignedPersonRepository.createAssignedPerson(AssignedPerson.builder()
                    .projectId(projectId).memberId(newPmId).role(AssignedPersonRole.PM).build());
        }

        List<Long> membersToAdd = new ArrayList<>(newUserIds);
        membersToAdd.removeAll(existingUserIds);

        List<Long> membersToRemove = new ArrayList<>(existingUserIds);
        membersToRemove.removeAll(newUserIds);

        if (newPmId != null) {
            membersToRemove.remove(newPmId);
        }

        if (!membersToRemove.isEmpty()) {
            assignedPersonRepository.deleteByProjectIdAndMemberIds(projectId, membersToRemove);
        }

        for(Long memberId : membersToRemove){
            List<Long> issueIds = issueRepository
                    .findAllByProjectIdAndMemberId(projectId, memberId)
                    .stream()
                    .map(Issue::getId)
                    .toList();
            if(issueIds != null && !issueIds.isEmpty()){
                issueRepository.unassignIssues(issueIds);
            }
        }

        if (!membersToAdd.isEmpty()) {
            for (Long memberId : membersToAdd) {
                assignedPersonRepository.createAssignedPerson(AssignedPerson.builder()
                        .projectId(projectId).memberId(memberId).role(AssignedPersonRole.USER).build());
            }
        }

        return getProject(projectId);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        List<AssignedPersonDetailResponseDTO> members = assignedPersonRepository.findMembersByProjectId(
                projectId);
        projectRepository.deleteById(projectId);

        if(members!=null && !members.isEmpty()){
            assignedPersonRepository.deleteByProjectIdAndMemberIds(
                    projectId,
                    members.stream()
                            .map(AssignedPersonDetailResponseDTO::getId)
                            .toList()
            );
        }

        List<Long> issueIds = issueRepository.findAllByProjectId(projectId).stream().map(Issue::getId).toList();

        if(!issueIds.isEmpty()){
            issueRepository.deleteIssuesByIds(issueIds);
        }
    }

    @Override
    public ProjectDetailResponseDTO addProjectMember(Long projectId, Long memberId) {
        assignedPersonRepository.createAssignedPerson(AssignedPerson.builder()
                .projectId(projectId)
                .memberId(memberId)
                .role(AssignedPersonRole.USER)
                .build());
        return getProject(projectId);
    }

    @Override
    public void deleteProjectMember(Long projectId, Long memberId) {
        assignedPersonRepository.deleteByProjectIdAndMemberIds(projectId, List.of(memberId));
    }

    private void createAssignedPersons(List<Long> memberIds, Long pmId, Long projectId) {
        List<AssignedPerson> assignedPersons = new ArrayList<>();

        if (memberIds != null) {
            for (Long memberId : memberIds) {
                if (memberId.equals(pmId)) {
                    continue;
                }
                assignedPersons.add(AssignedPerson.builder()
                        .projectId(projectId)
                        .memberId(memberId)
                        .role(AssignedPersonRole.USER)
                        .build());
            }
        }

        if (pmId != null && pmId != 0) {
            assignedPersons.add(AssignedPerson.builder()
                    .projectId(projectId)
                    .memberId(pmId)
                    .role(AssignedPersonRole.PM)
                    .build());
        }

        if (!assignedPersons.isEmpty()) {
            for (AssignedPerson assignedPerson : assignedPersons) {
                if(assignedPersonRepository.createAssignedPerson(assignedPerson) == 0) {
                    throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
                }
            }
        }
    }

}
