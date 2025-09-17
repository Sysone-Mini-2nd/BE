package com.sys.stm.domains.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDTO {
    private Long memberId;
    private Long parentId;
    private String content;
}
