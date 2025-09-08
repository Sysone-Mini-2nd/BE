package com.sys.stm.domains.project;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ProjectController {
    private final IssueService issueService;

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
}
