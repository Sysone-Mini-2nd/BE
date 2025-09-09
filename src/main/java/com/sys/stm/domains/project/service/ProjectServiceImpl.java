package com.sys.stm.domains.project.service;

import com.sys.stm.domains.assignedPerson.dao.AssignedPersonRepository;
import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import com.sys.stm.domains.assignedPerson.domain.AssignedPersonRole;
import com.sys.stm.domains.project.dao.ProjectRepository;
import com.sys.stm.domains.project.domain.Project;
import com.sys.stm.domains.project.domain.ProjectStatus;
import com.sys.stm.domains.project.dto.request.ProjectCreateRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectUpdateRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectStatsResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectSummaryResponseDTO;
import java.util.ArrayList;
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

    @Override
    public ProjectDetailResponseDTO getProject(Long projectId) {
        Project responseProject = projectRepository.findById(projectId);

        List<Map<String, Object>> membersByProjectId = assignedPersonRepository.findMembersByProjectId(projectId); // 완성되면 List<Member> 구조

        String pmName = "";
        for(Map<String, Object> member : membersByProjectId){
            String role = member.get("ROLE").toString();

            if(role.equals("PM")){
                pmName = member.get("NAME").toString();
            }
        }

        ProjectDetailResponseDTO responseDTO = ProjectDetailResponseDTO.builder()
                .id(responseProject.getId())
                .name(responseProject.getName())
                .desc(responseProject.getDesc())
                .status(responseProject.getStatus())
                .priority(responseProject.getPriority())
                .totalMemberCount(
                        membersByProjectId.size())
                .startDate(responseProject.getStartDate())
                .endDate(responseProject.getEndDate())
                .pmName(pmName)
                .members(membersByProjectId)
                .build();

        return responseDTO;
    }

    @Override
    public ProjectListResponseDTO getProjectsByMemberId(Long memberId) {
        // 1. [쿼리 1] 사용자가 속한 프로젝트 목록 조회 (N개)
        List<Project> responseProjects = projectRepository.findAllByMemberId(memberId);

        if (responseProjects.isEmpty()) {
            return ProjectListResponseDTO.builder().build(); // 빈 리스트 반환
        }

        // 2. 통계 조회를 위해 프로젝트 ID 목록 추출
        List<Long> projectIds = responseProjects.stream()
                .map(Project::getId)
                .toList(); // Java 16+ .collect(Collectors.toList());

        // 3. [쿼리 2] N개 프로젝트의 통계 정보를 한 번에 조회 (Batch Fetching)
        List<ProjectStatsResponseDTO> statsList = projectRepository.findProjectStatsByIds(projectIds);

        // 4. 통계 정보를 ID 기준으로 Map으로 변환 (빠른 조회를 위함)
        Map<Long, ProjectStatsResponseDTO> statsMap = statsList.stream()
                .collect(Collectors.toMap(ProjectStatsResponseDTO::getId, stats -> stats));

        // 5. DTO 조립 (DB 접근 없이 메모리에서 처리)
        List<ProjectSummaryResponseDTO> dtoList = new ArrayList<>();
        Map<ProjectStatus, Integer> statusCountMap = new HashMap<>();

        for (Project p : responseProjects) {
            // Map에서 해당 프로젝트의 통계 정보 가져오기
            ProjectStatsResponseDTO stats = statsMap.getOrDefault(p.getId(), new ProjectStatsResponseDTO()); // 통계 정보가 없는 경우 대비

            // 상태별 카운트 (기존 로직)
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
                    .pmName("") // todo: pmname 할당
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

        /*
        * 1. 프로젝트 생성
        * 2. 참여자에 insert
        */
        Project requestProject = Project.builder()
                .name(projectCreateRequestDTO.getName())
                .desc(projectCreateRequestDTO.getDesc())
                .status(projectCreateRequestDTO.getStatus())
                .priority(projectCreateRequestDTO.getPriority())
                .startDate(projectCreateRequestDTO.getStartDate())
                .endDate(projectCreateRequestDTO.getEndDate())
                .build();

        projectRepository.createProject(requestProject);

        List<AssignedPerson> assignedPersons = new ArrayList<>();

        for (Long memberId : projectCreateRequestDTO.getMemberIds()) {
            assignedPersons.add(AssignedPerson.builder()
                            .projectId(requestProject.getId())
                            .memberId(memberId)
                            .role(AssignedPersonRole.USER)
                    .build());
        }

        assignedPersons.add(AssignedPerson.builder()
                .projectId(requestProject.getId())
                .memberId(projectCreateRequestDTO.getPmId())
                .role(AssignedPersonRole.PM)
                .build());

        for(AssignedPerson assignedPerson : assignedPersons){
            System.out.println(assignedPerson);
            assignedPersonRepository.createAssignedPerson(assignedPerson);
        }

        // todo: 반환타입 처리, 요구사항 명세서 처리
        return null;
    }

    @Override
    public ProjectDetailResponseDTO updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDTO) {
        return null;
    }

    @Override
    public void deleteProject(Long projectId) {

    }
}
