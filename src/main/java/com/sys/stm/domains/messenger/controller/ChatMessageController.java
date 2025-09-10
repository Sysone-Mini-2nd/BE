package com.sys.stm.domains.messenger.controller;

import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.service.ChatMessageServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageServiceImpl chatMessageServiceImpl;

    // 메시지 수신
    @MessageMapping("/send")
    public void sendMessage(ChatMessageRequestDto message) {

        // 1. DB에 저장
        chatMessageServiceImpl.createMessage(message);

        // 2. 구독자들에게 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + message.getChatRoomId(), message);
    }
}
