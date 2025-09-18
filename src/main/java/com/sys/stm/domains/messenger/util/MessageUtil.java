package com.sys.stm.domains.messenger.util;

import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 작성자: 조윤상 */
@RequiredArgsConstructor
@Component
public class MessageUtil {

    private final ChatMessageService chatMessageService;

    public void createMessage(long chatRoomId, String content, long senderId, String type) {
        chatMessageService.createMessage(ChatMessageRequestDto.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .type(type)
                .build());
    }
}