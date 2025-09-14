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

    @GetMapping("/all")
    public ApiResponse<List<ChatRoomDataResponseDto>> findAllChatRoomsDataById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.ok(chatRoomService.findAllChatRoomsDataById(userDetails.getId()));
    }

    @GetMapping("/{chatRoomId}/messages")
    public ApiResponse<List<ChatMessageResponseDto>> getMessagesByChatRoomId(
            @PathVariable long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "50") int size) {
        List<ChatMessageResponseDto> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId, userDetails.getId(), page, size);
        return ApiResponse.ok(messages);
    }

    @GetMapping("/{chatRoomId}/mark-all-as-read")
    public ApiResponse<?> markAllMessagesAsRead(@PathVariable long chatRoomId,
                                                  @RequestParam Long lastReadMessageId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        messageStatusService.markAllMessagesInChatRoomAsRead(chatRoomId, userDetails.getId(), lastReadMessageId);
        return ApiResponse.ok();
    }

    @PostMapping("/create")
    public ApiResponse<String> createChatRoom(@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.validateChatRoomData(chatRoomCreateRequestDto);
        ChatRoom chatRoom = chatRoomCreateRequestDto.toChatRoom();
        chatRoomService.createChatRoom(chatRoom);
        chatRoomService.inviteMembers(chatRoomCreateRequestDto.getMemberIdList(), chatRoom.getId(), userDetails.getId());
        return ApiResponse.created();
    }

    @PostMapping("/invite")
    public ApiResponse<?> inviteMembers(@RequestBody ChatRoomInvitationRequestDto chatRoomInvitationRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.inviteMembers(chatRoomInvitationRequestDto.getMemberIdList(),
                chatRoomInvitationRequestDto.getChatRoomId(),
                userDetails.getId());
        return ApiResponse.created();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateChatRoom(@PathVariable long id, @RequestBody ChatRoomUpdateRequestDto dto) {
        int updateChatRoomCount = chatRoomService.updateChatRoom(id, dto);
        if (updateChatRoomCount == 1)
            return ApiResponse.ok();
        else
            throw new RuntimeException("채팅방 업데이트에 실패했습니다. 다시 시도해주세요.");
    }

    @DeleteMapping("/delete/{chatRoomId}")
    public ApiResponse<?> deleteFromChatRoom(@PathVariable long chatRoomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        int deleteCount = chatRoomParticipantService.deleteFromChatRoom(chatRoomId, userDetails.getId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/messages/{messageId}")
    public ApiResponse<?> deleteMessage(@PathVariable long messageId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatMessageResponseDto deletedMessageDto = chatMessageService.deleteMessage(userDetails.getId(), messageId);
        messagingTemplate.convertAndSend("/topic/chatroom/" + deletedMessageDto.getChatRoomId(), deletedMessageDto);
        return ApiResponse.ok();
    }

    @GetMapping("/total-unread-count")
    public ApiResponse<TotalUnreadCountResponseDto> getTotalUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long totalCount = chatRoomService.getTotalUnreadCount(userDetails.getId());
        return ApiResponse.ok(new TotalUnreadCountResponseDto(totalCount));
    }
}