package com.sys.stm.domains.issue.controller;

import com.sys.stm.domains.comment.dto.request.CommentCreateRequestDTO;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponseDTO;
import com.sys.stm.domains.comment.service.CommentService;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.service.IssueService;
import com.sys.stm.global.common.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ApiResponse<IssueDetailResponseDTO> getIssue(@PathVariable Long issueId) {
        return ApiResponse.ok(issueService.getIssue(issueId));
    }

    @PatchMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponseDTO> patchIssue(
            @PathVariable Long issueId,
            @RequestBody IssuePatchRequestDTO issuePatchRequestDTO
    ) {
        return ApiResponse.ok(issueService.updateIssueStatus(issueId, issuePatchRequestDTO));
    }

    @PutMapping("issues/{issueId}")
    public ApiResponse<IssueDetailResponseDTO> updateIssue(
            @PathVariable Long issueId,
            @RequestBody IssueUpdateRequestDTO issueUpdateRequestDTO
    ) {
        return ApiResponse.ok(issueService.updateIssue(issueId, issueUpdateRequestDTO));
    }

    @DeleteMapping("issues/{issueId}")
    public ApiResponse<String> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ApiResponse.ok();
    }

    @GetMapping("issues/{issueId}/comments")
    public ApiResponse<List<CommentDetailResponseDTO>> getComments(@PathVariable Long issueId) {
        return ApiResponse.ok(commentService.getCommentsByIssueId(issueId));
    }

    @PostMapping("issues/{issueId}/comments")
    public ApiResponse<CommentDetailResponseDTO> createComment(
            @PathVariable Long issueId,
            @RequestBody CommentCreateRequestDTO commentCreateRequestDTO
    ) {
        return ApiResponse.ok(commentService.createIssueComment(issueId, commentCreateRequestDTO));
    }
}
