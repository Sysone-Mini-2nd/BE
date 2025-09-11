package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;

import java.util.List;

public interface ChatMessageService {

    // 한 채팅방의 모든 메시지(페이지네이션)
    List<ChatMessageResponseDto> getMessagesByChatRoomId(long chatRoomId, long memberId, int page, int size);

    // 본인이 보낸 메시지 삭제
    int deleteMessagesById(long memberId, List<Long> messageIdList);

    ChatMessageResponseDto createMessage(ChatMessageRequestDto message);

    }