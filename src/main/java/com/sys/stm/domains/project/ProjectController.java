package com.sys.stm.domains.project;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequest;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;
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
    public ApiResponse<IssueListResponse> getIssuesByProjectId(
            @PathVariable Long projectId,
            IssueListRequest issueListRequest
    ) {
        return ApiResponse.ok(issueService.getProjectIssues(projectId, issueListRequest));
    }

    @PostMapping("projects/{projectId}/issues")
    public ApiResponse<IssueDetailResponse> createIssueByProjectId(
            @PathVariable Long projectId,
            @RequestBody IssueCreateRequest issueCreateRequest
    ) {
        return ApiResponse.ok(issueService.createProjectIssue(projectId, issueCreateRequest));
    }
}
