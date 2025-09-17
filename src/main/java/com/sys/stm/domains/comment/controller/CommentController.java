package com.sys.stm.domains.comment.controller;

import com.sys.stm.domains.comment.dto.request.CommentUpdateRequestDTO;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponseDTO;
import com.sys.stm.domains.comment.service.CommentService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/** 작성자: 백승준 */
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PatchMapping("comments/{commentId}")
    public ApiResponse<CommentDetailResponseDTO> patchComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO
            ) {
        return ApiResponse.ok(commentService.updateCommentContent(commentId, commentUpdateRequestDTO));
    }

    @DeleteMapping("comments/{commentId}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ApiResponse.okWithoutData();
    }
}
