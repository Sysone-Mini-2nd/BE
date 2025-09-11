package com.sys.stm.domains.messenger.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageStatus extends BaseEntity {
    private Long id;
    private Long messageId;
    private Long readerId;
    private LocalDateTime readAt;
}
