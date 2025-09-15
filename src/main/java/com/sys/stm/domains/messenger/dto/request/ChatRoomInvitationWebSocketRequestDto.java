package com.sys.stm.domains.messenger.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomInvitationWebSocketRequestDto {
    private Long chatRoomId;
    private Long memberId;
    private List<Long> memberIdList;
}