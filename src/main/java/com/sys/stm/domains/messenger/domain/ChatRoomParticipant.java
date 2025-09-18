package com.sys.stm.domains.messenger.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
/** 작성자: 조윤상 */
@Getter @Setter
@Builder
public class ChatRoomParticipant {
    private long id;
    private long memberId;
    private long chatRoomId;
    private Timestamp lastReadAt;
}
