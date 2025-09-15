package com.sys.stm.domains.messenger.controller;


import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.dto.request.ChatRoomCreateRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomInvitationRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomDataResponseDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.dto.response.TotalUnreadCountResponseDto;
import com.sys.stm.domains.messenger.service.ChatMessageServiceImpl;
import com.sys.stm.domains.messenger.service.ChatRoomParticipantService;
import com.sys.stm.domains.messenger.service.ChatRoomService;
import com.sys.stm.domains.messenger.service.MessageStatusService;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomParticipantService chatRoomParticipantService;
    private final ChatMessageServiceImpl chatMessageService;
    private final MessageStatusService messageStatusService;
    private final SimpMessagingTemplate messagingTemplate;

    // 본인이 속한 모든 채팅방 조회
    @GetMapping("/all")
    public ApiResponse<List<ChatRoomDataResponseDto>> findAllChatRoomsDataById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(chatRoomService.findAllChatRoomsDataById(userDetails.getId()));
    }

    // 채팅방의 모든 메시지 조회 (페이지네이션)
    @GetMapping("/{chatRoomId}/messages")
    public ApiResponse<List<ChatMessageResponseDto>> getMessagesByChatRoomId(
            @PathVariable long chatRoomId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();

        List<ChatMessageResponseDto> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId, memberId, page, size);
        return ApiResponse.ok(messages);
    }

    // 채팅방 입장 시 모든 메시지를 읽음으로 표시
    @GetMapping("/{chatRoomId}/mark-all-as-read")
    public ApiResponse<?> markAllMessagesAsRead(@PathVariable long chatRoomId
            , @RequestParam Long lastReadMessageId
            , @AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();
        messageStatusService.markAllMessagesInChatRoomAsRead(chatRoomId, memberId, lastReadMessageId);
        return ApiResponse.ok();
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ApiResponse<String> createChatRoom(@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto
            ,@AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();

        System.out.println("--- Debugging Backend Chat Room Creation ---");
        System.out.println("Received DTO name: " + chatRoomCreateRequestDto.getName());
        System.out.println("Received DTO memberIdList: " + chatRoomCreateRequestDto.getMemberIdList());
        System.out.println("Received DTO type: " + chatRoomCreateRequestDto.getType());
        System.out.println("Authenticated User ID (creator): " + userDetails.getId());
        System.out.println("------------------------------------------");

        // 채팅방 검증(1. 1:1 메시지인데 채팅방 참여 인원이 여러 명인지, 2. 채팅방 참여 인원 id 리스트가 실제로 존재하는 사람인지)
        chatRoomService.validateChatRoomData(chatRoomCreateRequestDto);

        // 채팅방 생성
        ChatRoom chatRoom = chatRoomCreateRequestDto.toChatRoom();
        chatRoomService.createChatRoom(chatRoom);

        // 채팅방에 초대 기능 구현
        chatRoomService.inviteMembers(chatRoomCreateRequestDto.getMemberIdList(), chatRoom.getId(), memberId);

        return ApiResponse.created();
    }

    // 이미 존재하는 채팅방에 초대
    @PostMapping("/invite")
    public ApiResponse<?> inviteMembers(@RequestBody ChatRoomInvitationRequestDto chatRoomInvitationRequestDto
            , @AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();

        // 채팅방에 초대 기능 구현
        chatRoomService.inviteMembers(chatRoomInvitationRequestDto.getMemberIdList()
                , chatRoomInvitationRequestDto.getChatRoomId()
                , memberId);

        return ApiResponse.created();
    }


    // 채팅방 이름 or 최근 메시지 변경
    @PutMapping("/update/{id}")
    public ApiResponse<?> updateChatRoom(@PathVariable long id, @RequestBody ChatRoomUpdateRequestDto dto
    ) {

        int updateChatRoomCount = chatRoomService.updateChatRoom(id, dto);

        if (updateChatRoomCount == 1)
            return ApiResponse.ok();
        else
            throw new RuntimeException("채팅방 업데이트에 실패했습니다. 다시 시도해주세요.");
    }

    @DeleteMapping("/delete/{chatRoomId}")
    public ApiResponse<?> deleteFromChatRoom(@PathVariable long chatRoomId,@AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();
        int deleteCount = chatRoomParticipantService.deleteFromChatRoom(chatRoomId, memberId);

        return ApiResponse.ok();
    }


    @DeleteMapping("/messages/{messageId}")
    public ApiResponse<?> deleteMessage(@PathVariable long messageId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();

        // 1. 서비스 호출하여 메시지 삭제 처리 및 업데이트된 DTO 받기
        ChatMessageResponseDto deletedMessageDto = chatMessageService.deleteMessage(memberId, messageId);

        // 2. WebSocket으로 삭제된 메시지 정보 전송
        messagingTemplate.convertAndSend("/topic/chatroom/" + deletedMessageDto.getChatRoomId(),
                deletedMessageDto);

        return ApiResponse.ok();
    }

    // 전체 안 읽은 메시지 개수 조회
    @GetMapping("/total-unread-count")
    public ApiResponse<TotalUnreadCountResponseDto> getTotalUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();

        long totalCount = chatRoomService.getTotalUnreadCount(memberId);
        return ApiResponse.ok(new TotalUnreadCountResponseDto(totalCount));

    }

}