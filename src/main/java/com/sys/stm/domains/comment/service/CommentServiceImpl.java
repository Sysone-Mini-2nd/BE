package com.sys.stm.domains.comment.service;

import com.sys.stm.domains.comment.dao.CommentRepository;
import com.sys.stm.domains.comment.domain.Comment;
import com.sys.stm.domains.comment.dto.request.CommentCreateRequestDTO;
import com.sys.stm.domains.comment.dto.request.CommentUpdateRequestDTO;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponseDTO;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDetailResponseDTO> getCommentsByIssueId(Long issueId) {
        List<CommentDetailResponseDTO> comments = commentRepository.findCommentsByIssueId(issueId);

        // ID -> 댓글 매핑
        Map<Long, CommentDetailResponseDTO> commentMap = new HashMap<>();
        List<CommentDetailResponseDTO> roots = new ArrayList<>();

        for (CommentDetailResponseDTO comment : comments) {
            comment.setChildren(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
        }

        for (CommentDetailResponseDTO comment : comments) {
            if (comment.getParentId() == null) {
                roots.add(comment); // 부모 댓글
            } else {
                CommentDetailResponseDTO parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment); // 부모의 children에 추가
                }
            }
        }

        return roots;
    }

    @Override
    public CommentDetailResponseDTO createIssueComment(Long issueId, CommentCreateRequestDTO commentCreateRequestDTO){
        Comment requestComment = Comment.builder()
                .issueId(issueId)
                .content(commentCreateRequestDTO.getContent())
                .parentId(commentCreateRequestDTO.getParentId())
                .memberId(commentCreateRequestDTO.getMemberId())
                .build();

        if(commentRepository.createComment(requestComment) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return commentRepository.findCommentById(requestComment.getId());
    }

    @Override
    public CommentDetailResponseDTO updateCommentContent(Long commentId, CommentUpdateRequestDTO commentUpdateRequestDTO) {
        Comment requestComment = Comment.builder()
                .id(commentId)
                .content(commentUpdateRequestDTO.getContent())
                .build();

        if(commentRepository.updateCommentContent(requestComment) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return commentRepository.findCommentById(requestComment.getId());
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment requestComment = Comment.builder()
                .id(commentId)
                .build();

        if(commentRepository.deleteCommentById(requestComment) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }
    }
}
