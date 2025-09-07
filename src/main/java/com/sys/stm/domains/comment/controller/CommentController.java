package com.sys.stm.domains.comment.controller;

import com.sys.stm.domains.comment.dto.request.CommentUpdateRequest;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponse;
import com.sys.stm.domains.comment.service.CommentService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PatchMapping("comment/{commentId}")
    public ApiResponse<CommentDetailResponse> updateIssue(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest
            ) {
        return ApiResponse.ok(commentService.updateCommentContent(commentId, commentUpdateRequest));
    }
}
