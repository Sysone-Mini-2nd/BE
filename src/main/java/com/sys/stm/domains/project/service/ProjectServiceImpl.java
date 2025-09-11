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
import com.sys.stm.domains.project.dto.request.ProjectUpdateRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;
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

        List<Map<String, Object>> membersByProjectId = assignedPersonRepository.findMembersByProjectId(projectId); // todo: List<Member>로 변경

        String pmName = "";
        long pmId = 0L;
        for(Map<String, Object> member : membersByProjectId){
            String role = member.get("ROLE").toString();

            if(role.equals("PM")){
                pmName = member.get("NAME").toString();
                pmId = Long.parseLong(member.get("ID").toString());
            }
        }

        return ProjectDetailResponseDTO.builder()
                .id(responseProject.getId())
                .name(responseProject.getName())
                .desc(responseProject.getDesc())
                .status(responseProject.getStatus())
                .priority(responseProject.getPriority())
                .totalMemberCount(
                        membersByProjectId.size())
                .startDate(responseProject.getStartDate())
                .endDate(responseProject.getEndDate())
                .pmId(pmId)
                .pmName(pmName)
                .members(membersByProjectId)
                .build();
    }

    @Override
    public ProjectListResponseDTO getProjectsByMemberId(Long memberId) {
        List<Project> responseProjects = projectRepository.findAllByMemberId(memberId);

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

        for(IssueCreateRequestDTO dto : dtoList){
            issueRepository.createIssue(Issue.builder()
                            .projectId(requestProject.getId())
                            .title(dto.getTitle())
                            .desc(dto.getDesc())
                            .status(IssueStatus.TODO)
                            .priority(IssuePriority.NORMAL)
                            .startDate(Timestamp.valueOf(LocalDateTime.now()))
                            .endDate(Timestamp.valueOf(LocalDateTime.now()))
                    .build());
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

        List<Map<String, Object>> currentMembers = assignedPersonRepository.findMembersByProjectId(projectId);
        long existingPmId = 0L;
        List<Long> existingUserIds = new ArrayList<>();
        for (Map<String, Object> member : currentMembers) {
            if ("PM".equals(member.get("ROLE").toString())) {
                existingPmId = Long.parseLong(member.get("ID").toString());
            } else {
                existingUserIds.add(Long.parseLong(member.get("ID").toString()));
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
            issueRepository.unassignIssues(issueIds);
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
        List<Map<String, Object>> members = assignedPersonRepository.findMembersByProjectId(projectId);
        projectRepository.deleteById(projectId);

        if(members!=null && !members.isEmpty()){
            assignedPersonRepository.deleteByProjectIdAndMemberIds(
                    projectId,
                    members.stream()
                            .map(member -> Long.parseLong(member.get("ID").toString()))
                            .toList()
            );
        }

        List<Long> issueIds = issueRepository.findAllByProjectId(projectId).stream().map(Issue::getId).toList();

        if(!issueIds.isEmpty()){
            issueRepository.deleteIssuesByIds(issueIds);
        }
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
