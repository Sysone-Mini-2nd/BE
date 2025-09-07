package com.sys.stm.domains.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private Long memberId;
    private Long parentId;
    private String content;
}
