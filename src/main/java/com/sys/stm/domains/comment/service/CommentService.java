package com.sys.stm.domains.comment.service;

import com.sys.stm.domains.comment.dto.request.CommentCreateRequestDTO;
import com.sys.stm.domains.comment.dto.request.CommentUpdateRequestDTO;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponseDTO;
import java.util.List;

public interface CommentService {
    List<CommentDetailResponseDTO> getCommentsByIssueId(Long issueId);
    CommentDetailResponseDTO createIssueComment(Long issueId, CommentCreateRequestDTO commentCreateRequestDTO);
    CommentDetailResponseDTO updateCommentContent(Long commentId, CommentUpdateRequestDTO commentUpdateRequestDTO);
    void deleteComment(Long commentId);

}
