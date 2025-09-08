package com.sys.stm.domains.comment.service;

import com.sys.stm.domains.comment.dto.request.CommentCreateRequest;
import com.sys.stm.domains.comment.dto.request.CommentUpdateRequest;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponse;
import java.util.List;

public interface CommentService {
    List<CommentDetailResponse> getCommentsByIssueId(Long issueId);
    CommentDetailResponse createIssueComment(Long issueId, CommentCreateRequest commentCreateRequest);
    CommentDetailResponse updateCommentContent(Long commentId, CommentUpdateRequest commentUpdateRequest);
    void deleteComment(Long commentId);

}
