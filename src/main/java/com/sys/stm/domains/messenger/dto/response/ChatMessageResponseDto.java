package com.sys.stm.domains.messenger.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
public class ChatMessageResponseDto {
    private long id;
    private long chatRoomId;
    private String senderName; // 보낸 사람 이름 추가
    private String type;
    private String content;
    private String fileUrl;
    private LocalDateTime createdAt;
    // 자신이 보낸 메시지인지 아닌지
    @JsonProperty("isMine")  // JSON 필드명을 명시
    private boolean isMine;
    // 메시지를 안 읽은 사람 수 (readCount)
    private long readCount;
    @JsonProperty("senderId")  // JSON 필드명을 명시
    private long senderId;
}

