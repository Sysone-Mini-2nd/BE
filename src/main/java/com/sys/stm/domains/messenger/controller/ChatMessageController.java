package com.sys.stm.domains.messenger.controller;

import com.sys.stm.domains.member.dao.MemberRepository;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.domain.Message;
import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomInvitationRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomInvitationWebSocketRequestDto;
import com.sys.stm.domains.messenger.dto.request.MessageReadRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomListUpdateResponseDto;
import com.sys.stm.domains.messenger.dto.response.MessageReadResponseDto;
import com.sys.stm.domains.messenger.dto.response.TotalUnreadCountResponseDto;
import com.sys.stm.domains.messenger.service.ChatMessageServiceImpl;
import com.sys.stm.domains.messenger.service.ChatRoomParticipantService;
import com.sys.stm.domains.messenger.service.ChatRoomService;
import com.sys.stm.domains.messenger.service.MessageStatusService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/** 작성자: 조윤상 */
@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageServiceImpl chatMessageServiceImpl;
    private final MessageStatusService messageStatusService;
    private final ChatRoomParticipantService chatRoomParticipantService;
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;

    // 메시지 수신
    @MessageMapping("/send")
    public void sendMessage(ChatMessageRequestDto message) {

        // 메시지를 DB에 저장
        ChatMessageResponseDto chatMessageResponseDto = chatMessageServiceImpl.createMessage(message);

        // 메시지를 구독자들(채팅방 참여자)에게 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + message.getChatRoomId(), chatMessageResponseDto);

        // 채팅방 참여자들에게 최신 전체 안읽음 카운트 전송
        List<Long> participantIds =
                chatRoomParticipantService.findParticipantIdsByRoomId(message.getChatRoomId());

        // 채팅방의 최신 메시지 조회
        String recentMessage = chatRoomService.getRecentMessageById(message.getChatRoomId());

        for (Long participantId : participantIds) {
            if (participantId.equals(message.getSenderId())) {
                continue; // 메시지 보낸 사람은 제외
            }

            // 해당 참여자의 전체 안 읽은 메시지 개수를 다시 계산
            long totalUnreadCount = chatRoomService.getTotalUnreadCount(participantId);

            // 개인 큐로 최신 총합 전송
            messagingTemplate.convertAndSend(
                    "/topic/update",
                    new TotalUnreadCountResponseDto(totalUnreadCount, recentMessage, participantId, message.getChatRoomId())
            );
        }

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

    @MessageMapping("/invite")
    public void inviteMembersToChatRoom(ChatRoomInvitationWebSocketRequestDto request) {
        // Extract data from the request
        Long chatRoomId = request.getChatRoomId();
        Long memberId = request.getMemberId();
        List<Long> memberIdList = request.getMemberIdList();

        // Log the incoming request
        System.out.println("--- Invite Members to Chat Room ---");
        System.out.println("Chat Room ID: " + chatRoomId);
        System.out.println("Inviter Member ID: " + memberId);
        System.out.println("Invited Member IDs: " + memberIdList);
        System.out.println("------------------------------------");

        // Add members to the chat room
        LocalDateTime currentTime = LocalDateTime.now();
        List<ChatRoomParticipant> chatRoomParticipants = memberIdList.stream()
                .map(id -> ChatRoomParticipant.builder()
                        .memberId(id)
                        .chatRoomId(chatRoomId)
                        .lastReadAt(Timestamp.valueOf(currentTime))
                        .build())
                .toList();
        chatRoomParticipantService.createChatRoomParticipants(chatRoomParticipants);

        // Fetch names of invited members
        List<String> nameList = memberIdList.stream()
                .map(id -> memberRepository.findMemberById(id).map(MemberResponseDTO::getName).orElse("Unknown"))
                .toList();

        // Create and send a system message
        Message message = messageStatusService.createInitialInvitationMessageforWebsocket(nameList, chatRoomId, memberId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoomId, ChatMessageResponseDto.builder()
                .chatRoomId(chatRoomId)
                .id(message.getId())
                .senderId(memberId)
                .senderName(memberRepository.findMemberById(memberId).map(MemberResponseDTO::getName).orElse("System"))
                .type(message.getType())
                .content(message.getContent())
                .createdAt(message.getCreatedAt().toLocalDateTime())
                .readCount(0)
                .build());
    }
}
