package com.sys.stm.domains.messenger.controller;

import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.request.MessageReadRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.dto.response.MessageReadResponseDto;
import com.sys.stm.domains.messenger.service.ChatMessageServiceImpl;
import com.sys.stm.domains.messenger.service.MessageStatusService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageServiceImpl chatMessageServiceImpl;
    private final MessageStatusService messageStatusService;

    // 메시지 수신
    @MessageMapping("/send")
    public void sendMessage(ChatMessageRequestDto message) {

        // 메시지를 DB에 저장
        ChatMessageResponseDto chatMessageResponseDto = chatMessageServiceImpl.createMessage(message);

        // 메시지를 구독자들(채팅방 참여자)에게 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + message.getChatRoomId(), chatMessageResponseDto);
    }


    // 개별 메시지를 읽음으로 표시 (WebSocket)
    @MessageMapping("/message/read")
    public void markMessageAsReadWebSocket(MessageReadRequestDto messageReadRequestDto) {

        messageStatusService.markMessageAsRead(
                messageReadRequestDto.getMemberId(),
                messageReadRequestDto.getChatRoomId(),
                messageReadRequestDto.getMessageId()
        );

        // 채팅방의 다른 참여자에게도 읽음 이벤트 전파
        messagingTemplate.convertAndSend(
                "/topic/chat/" + messageReadRequestDto.getChatRoomId() + "/read",
                MessageReadResponseDto.builder()
                        .messageId(messageReadRequestDto.getMessageId())
                        .memberId(messageReadRequestDto.getMemberId())
                        .build()
        );
    }
}
