package com.sys.stm.domains.messenger.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/** 작성자: 조윤상 */
@Getter @Setter
@Builder
public class ChatRoom extends BaseEntity {

    private long id;
    private String name;
    private String type;
    private String recentMessage;
}
