package com.sys.stm.domains.project.controller;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.domains.project.dto.request.ProjectCreateRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectListRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectMemberChangeRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectUpdateRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;
import com.sys.stm.domains.project.service.ProjectService;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ProjectController {
    private final IssueService issueService;
    private final ProjectService projectService;

    @GetMapping("projects/{projectId}/issues")
    public ApiResponse<IssueListResponseDTO> getIssuesByProjectId(
            @PathVariable Long projectId,
            IssueListRequestDTO issueListRequestDTO
    ) {
        return ApiResponse.ok(issueService.getProjectIssues(projectId, issueListRequestDTO));
    }

    @PostMapping("projects/{projectId}/issues")
    public ApiResponse<IssueDetailResponseDTO> createIssueByProjectId(
            @PathVariable Long projectId,
            @RequestBody IssueCreateRequestDTO issueCreateRequestDTO
    ) {
        return ApiResponse.ok(issueService.createProjectIssue(projectId, issueCreateRequestDTO));
    }

    @GetMapping("projects")
    public ApiResponse<ProjectListResponseDTO> getProjectsByMemberId(
            ProjectListRequestDTO projectListRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.ok(projectService.getProjectsByMemberId(userDetails.getId(), projectListRequestDTO)); // todo: UserDetails.getMember.getId() 변경
    }
    
    @GetMapping("projects/{projectId}")
    public ApiResponse<ProjectDetailResponseDTO> getProjectByProjectId(
            @PathVariable Long projectId
    ) {
        return ApiResponse.ok(projectService.getProject(projectId));
    }

    @PostMapping("projects")
    public ApiResponse<ProjectDetailResponseDTO> createProject(
            @RequestBody ProjectCreateRequestDTO projectCreateRequestDTO
    ) {
        return ApiResponse.ok(projectService.createProject(projectCreateRequestDTO));
    }

    @PutMapping("projects/{projectId}")
    public ApiResponse<ProjectDetailResponseDTO> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequestDTO projectUpdateRequestDTO
    ) {
        return ApiResponse.ok(projectService.updateProject(projectId, projectUpdateRequestDTO));
    }

    @DeleteMapping("projects/{projectId}")
    public ApiResponse<String> deleteProject(
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return ApiResponse.ok();
    }

    @PostMapping("projects/{projectId}/members")
    public ApiResponse<ProjectDetailResponseDTO> addProjectMember(
            @PathVariable Long projectId,
            @RequestBody ProjectMemberChangeRequestDTO projectMemberChangeRequestDTO
            ) {
        return ApiResponse.ok(projectService.addProjectMember(projectId, projectMemberChangeRequestDTO.getMemberId()));
    }

    @DeleteMapping("projects/{projectId}/members")
    public ApiResponse<String> deleteProjectMember(
            @PathVariable Long projectId,
            @RequestBody ProjectMemberChangeRequestDTO projectMemberChangeRequestDTO
    ) {
        projectService.deleteProjectMember(projectId, projectMemberChangeRequestDTO.getMemberId());
        return ApiResponse.ok();
    }
}
