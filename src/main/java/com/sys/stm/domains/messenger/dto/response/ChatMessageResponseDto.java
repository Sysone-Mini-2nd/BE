package com.sys.stm.domains.messenger.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
public class ChatMessageResponseDto {
    private long id;
    private long chatRoomId;
    private String type;
    private String content;
    private String fileUrl;
    private LocalDateTime createdAt;
    // 자신이 보낸 메시지인지 아닌지 
    private boolean isMine;
}
