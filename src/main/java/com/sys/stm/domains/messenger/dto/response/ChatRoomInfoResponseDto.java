package com.sys.stm.domains.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomInfoResponseDto {

    // 채팅방 id
    private Long id;
    // 채팅방 이름
    private String name;
    // 채팅방 최근 메시지
    private String recentMessage;
    // 해당 채팅방에서 아직 안 읽은 메시지 수
    private long unreadMessageCount;
    // 최근 메시지가 생성된 시간
    private LocalDateTime messageCreatedAt;

}
