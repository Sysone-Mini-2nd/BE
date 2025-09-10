package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatMessageRepository;
import com.sys.stm.domains.messenger.dao.ChatRoomRepository;
import com.sys.stm.domains.messenger.domain.Message;
import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository chatMessageRepository;

    // DB에 메시지 저장
    @Override
    public int createMessage(ChatMessageRequestDto message) {
        // TODO refactor 메시지 한 개 보낼때마다 DB 커넥션 연결하지 말고 모아서 한 번에 하기

        return chatMessageRepository.createMessage(message.toMessage());
    }


    @Override
    public List<ChatMessageResponseDto> getMessagesByChatRoomId(long chatRoomId, long memberId, int page, int size) {
        // 페이지와 사이즈를 레포지토리로 전달
        List<Message> messageList = chatMessageRepository.getMessagesByChatRoomId(chatRoomId, page, size);
        return messageList.stream().map((message) -> {
            return message.toChatMessageResponseDto(memberId);
        }).toList();
    }

    // 본인이 보낸 메시지 삭제
    @Override
    public int deleteMessagesById(long memberId, List<Long> messageIdList) {
        return 0;
    }
}
