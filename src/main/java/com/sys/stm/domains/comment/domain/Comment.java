package com.sys.stm.domains.comment.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Comment extends BaseEntity{
    private Long id;
    private Long issueId;
    private Long parentId;
    private String content;
    private Long memberId;
}