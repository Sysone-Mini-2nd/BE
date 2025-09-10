package com.sys.stm.domains.messenger.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Builder
public class ChatRoomDataResponseDto {

    private Long id;
    private String name;
    private String recentMessage;
    private List<String> memberNameList;
    private long unreadMessageCount;
    private LocalDateTime messageCreatedAt;

}
