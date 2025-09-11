package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatMessageRepository;
import com.sys.stm.domains.messenger.domain.Message;
import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.dto.response.MessageQueryResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // DB에 메시지 저장
    @Override
    public ChatMessageResponseDto createMessage(ChatMessageRequestDto chatMessageRequestDto) {

        Message message = chatMessageRequestDto.toMessage();
        chatMessageRepository.createMessage(message);

        MessageQueryResultDto messageQueryResultDto = chatMessageRepository.findMessageById(message.getId());
        return ChatMessageResponseDto.builder()
                .id(messageQueryResultDto.getId())
                .chatRoomId(messageQueryResultDto.getChatRoomId())
                .content(messageQueryResultDto.getContent())
                .fileUrl(messageQueryResultDto.getFileUrl())
                .type(messageQueryResultDto.getType())
                .createdAt(messageQueryResultDto.getCreatedAt())
                .isMine(chatMessageRequestDto.getSenderId() == messageQueryResultDto.getSenderId())
                .readCount(messageQueryResultDto.getReadCount() - 1)
                .build();

    }


    @Override
    public List<ChatMessageResponseDto> getMessagesByChatRoomId(long chatRoomId, long memberId, int page, int size) {
        // 페이지와 사이즈를 레포지토리로 전달
        List<MessageQueryResultDto> messageList = chatMessageRepository.findMessagesByChatRoomId(chatRoomId, page, size);

        return messageList.stream().map((messageQueryResultDto) -> {
            return ChatMessageResponseDto.builder()
                    .id(messageQueryResultDto.getId())
                    .chatRoomId(messageQueryResultDto.getChatRoomId())
                    .content(messageQueryResultDto.getContent())
                    .fileUrl(messageQueryResultDto.getFileUrl())
                    .type(messageQueryResultDto.getType())
                    .createdAt(messageQueryResultDto.getCreatedAt())
                    .isMine(memberId == messageQueryResultDto.getSenderId())
                    .readCount(messageQueryResultDto.getReadCount() - 1)
                    .build();
        }).toList();
    }

    // 본인이 보낸 메시지 삭제
    @Override
    public int deleteMessagesById(long memberId, List<Long> messageIdList) {
        return 0;
    }
}
