package com.sys.stm.domains.issue.controller;

import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponse> getIssue(@PathVariable Long issueId) {
        return ApiResponse.ok(issueService.getIssue(issueId));
    }

    @GetMapping("projects/{projectId}/issues")
    public ApiResponse<IssueListResponse> getIssuesByProjectId(
            @PathVariable Long projectId,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String status
    ){
        return ApiResponse.ok(issueService.getProjectIssues(projectId, priority, assigneeId, status));
    }
}
