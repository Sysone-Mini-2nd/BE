package com.sys.stm.domains.comment.service;

import com.sys.stm.domains.comment.dao.CommentRepository;
import com.sys.stm.domains.comment.domain.Comment;
import com.sys.stm.domains.comment.dto.request.CommentCreateRequest;
import com.sys.stm.domains.comment.dto.request.CommentUpdateRequest;
import com.sys.stm.domains.comment.dto.response.CommentDetailResponse;
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
    public List<CommentDetailResponse> getCommentsByIssueId(Long issueId) {
        List<CommentDetailResponse> comments = commentRepository.findCommentsByIssueId(issueId);

        // ID -> 댓글 매핑
        Map<Long, CommentDetailResponse> commentMap = new HashMap<>();
        List<CommentDetailResponse> roots = new ArrayList<>();

        for (CommentDetailResponse comment : comments) {
            comment.setChildren(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
        }

        for (CommentDetailResponse comment : comments) {
            if (comment.getParentId() == null) {
                roots.add(comment); // 부모 댓글
            } else {
                CommentDetailResponse parent = commentMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getChildren().add(comment); // 부모의 children에 추가
                }
            }
        }

        return roots;
    }

    @Override
    public CommentDetailResponse createIssueComment(Long issueId, CommentCreateRequest commentCreateRequest){
        Comment requestComment = Comment.builder()
                .issueId(issueId)
                .content(commentCreateRequest.getContent())
                .parentId(commentCreateRequest.getParentId())
                .memberId(commentCreateRequest.getMemberId())
                .build();

        if(commentRepository.createComment(requestComment) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return commentRepository.findCommentById(requestComment.getId());
    }

    @Override
    public CommentDetailResponse updateCommentContent(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Comment requestComment = Comment.builder()
                .id(commentId)
                .content(commentUpdateRequest.getContent())
                .build();

        if(commentRepository.updateCommentContent(requestComment) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return commentRepository.findCommentById(requestComment.getId());
    }
}
