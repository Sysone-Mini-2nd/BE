package com.sys.stm.domains.issue.controller;

import com.sys.stm.domains.comment.dto.request.CommentCreateRequest;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponse;
import com.sys.stm.domains.comment.service.CommentService;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequest;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequest;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.global.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;
    private final CommentService commentService;

    @GetMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponse> getIssue(@PathVariable Long issueId) {
        return ApiResponse.ok(issueService.getIssue(issueId));
    }

    @GetMapping("projects/{projectId}/issues")
    public ApiResponse<IssueListResponse> getIssuesByProjectId(
            @PathVariable Long projectId,
            IssueListRequest issueListRequest
    ) {
        return ApiResponse.ok(issueService.getProjectIssues(projectId, issueListRequest));
    }

    @PostMapping("projects/{projectId}/issues")
    public ApiResponse<IssueDetailResponse> createIssue(
            @PathVariable Long projectId,
            @RequestBody IssueCreateRequest issueCreateRequest
    ) {
        return ApiResponse.ok(issueService.createProjectIssue(projectId, issueCreateRequest));
    }

    @PatchMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponse> patchIssue(
            @PathVariable Long issueId,
            @RequestBody IssuePatchRequest issuePatchRequest
    ) {
        return ApiResponse.ok(issueService.updateIssueStatus(issueId, issuePatchRequest));
    }

    @PutMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponse> updateIssue(
            @PathVariable Long issueId,
            @RequestBody IssueUpdateRequest issueUpdateRequest
    ) {
        return ApiResponse.ok(issueService.updateIssue(issueId, issueUpdateRequest));
    }

    @GetMapping("issues/{issueId}/comments")
    public ApiResponse<List<CommentDetailResponse>> getComments(@PathVariable Long issueId) {
        return ApiResponse.ok(commentService.getCommentsByIssueId(issueId));
    }

    @PostMapping("issues/{issueId}/comments")
    public ApiResponse<CommentDetailResponse> getComments(
            @PathVariable Long issueId,
            @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        return ApiResponse.ok(commentService.createIssueComment(issueId, commentCreateRequest));
    }
}
