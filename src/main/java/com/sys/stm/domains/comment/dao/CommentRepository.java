package com.sys.stm.domains.comment.dao;

import com.sys.stm.domains.comment.domain.Comment;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
/** 작성자: 백승준 */
@Mapper
public interface CommentRepository {
    // 댓글 단건 조회
    CommentDetailResponseDTO findCommentById(Long commentId);

    // 한 이슈에 대한 모든 댓글 가져오기
    List<CommentDetailResponseDTO> findCommentsByIssueId(Long issueId);

    // 댓글 추가
    int createComment(Comment comment);

    // 댓글 수정
    int updateCommentContent(Comment comment);

    // 댓글 삭제
    int deleteCommentById(Comment comment);
}
