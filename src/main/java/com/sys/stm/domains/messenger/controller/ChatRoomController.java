package com.sys.stm.domains.messenger.controller;


import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.request.ChatRoomCreateRequestDto;
import com.sys.stm.domains.messenger.dto.request.ChatRoomUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomDataResponseDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.service.ChatMessageServiceImpl;
import com.sys.stm.domains.messenger.service.ChatRoomParticipantService;
import com.sys.stm.domains.messenger.service.ChatRoomService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat-room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomParticipantService chatRoomParticipantService;
    private final ChatMessageServiceImpl chatMessageService;

    // 본인이 속한 모든 채팅방 조회
    @GetMapping("/all")
    public ApiResponse<List<ChatRoomDataResponseDto>> findAllChatRoomsDataById() {
        //TODO SecurityContextHolder에 있는 Member 객체 가져오기, 일단 지금은 member id 하드코딩
        long id = 1;
        return ApiResponse.ok(chatRoomService.findAllChatRoomsDataById(id));
    }

    // 채팅방의 모든 메시지 조회 (페이지네이션)
    @GetMapping("/{chatRoomId}/messages")
    public ApiResponse<List<ChatMessageResponseDto>> getChatRoomMessages(
            @PathVariable long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // TODO SecurityContextHolder에서 memberId 가져오기
        long memberId = 1; // 임시 memberId

        List<ChatMessageResponseDto> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId, memberId, page, size);
        return ApiResponse.ok(messages);
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ApiResponse<String> createChatRoom(@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto) {

        // 채팅방 검증(1. 1:1 메시지인데 채팅방 참여 인원이 여러 명인지, 2. 채팅방 참여 인원 id 리스트가 실제로 존재하는 사람인지)
        chatRoomService.validateChatRoomData(chatRoomCreateRequestDto);

        // 채팅방 생성
        ChatRoom chatRoom = chatRoomCreateRequestDto.toChatRoom();
        chatRoomService.createChatRoom(chatRoom);

        // 채팅방에 초대된 사원 리스트 저장
        List<ChatRoomParticipant> chatRoomParticipants = chatRoomCreateRequestDto.toChatRoomParticipants(chatRoom.getId());
        chatRoomParticipantService.createChatRoomParticipants(chatRoomParticipants);

        return ApiResponse.created();

    }

    // 채팅방 이름 or 최근 메시지 변경
    @PutMapping("/update/{id}")
    public ApiResponse<?> updateChatRoom(@PathVariable long id, @RequestBody ChatRoomUpdateRequestDto dto) {

        int updateChatRoomCount = chatRoomService.updateChatRoom(id, dto);

        if (updateChatRoomCount == 1)
            return ApiResponse.ok();
        else
            throw new RuntimeException("채팅방 업데이트에 실패했습니다. 다시 시도해주세요.");
    }

    @DeleteMapping("/delete/{chatRoomId}")
    public ApiResponse<?> deleteFromChatRoom(@PathVariable long chatRoomId) {
        //TODO SecurityContextHolder에 있는 Member 객체 가져오기, 일단 지금은 member id 하드코딩
        long memberId = 1;
        int deleteCount = chatRoomParticipantService.deleteFromChatRoom(chatRoomId, memberId);

        return ApiResponse.ok();
    }
}

