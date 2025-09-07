package com.sys.stm.domains.messenger.controller;


import com.sys.stm.domains.messenger.dto.response.ChatRoomDataResponseDto;
import com.sys.stm.domains.messenger.service.ChatRoomParticipantService;
import com.sys.stm.domains.messenger.service.ChatRoomService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/messenger/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomParticipantService chatRoomParticipantService;

    // 본인이 속한 모든 채팅방 조회
    @GetMapping("/all-chat-rooms")
    public ApiResponse<List<ChatRoomDataResponseDto>> findAllChatRoomsDataById(){
        //TODO SecurityContextHolder에 있는 Member 객체 가져오기, 일단 지금은 member id 하드코딩
        long id = 1;
        return ApiResponse.ok(chatRoomService.findAllChatRoomsDataById(id));
    }

}
