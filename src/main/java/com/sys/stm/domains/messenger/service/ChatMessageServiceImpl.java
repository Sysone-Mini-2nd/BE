package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.member.dao.MemberRepository;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;
import com.sys.stm.domains.messenger.dao.ChatMessageRepository;
import com.sys.stm.domains.messenger.domain.Message;
import com.sys.stm.domains.messenger.dto.request.ChatMessageRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.domains.messenger.dto.response.MessageQueryResultDto;
import com.sys.stm.global.exception.ForbiddenException;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sys.stm.global.exception.ExceptionMessage.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    // DB에 메시지 저장
    @Override
    public ChatMessageResponseDto createMessage(ChatMessageRequestDto chatMessageRequestDto) {

        Message message = chatMessageRequestDto.toMessage();
        chatMessageRepository.createMessage(message);

        MessageQueryResultDto messageQueryResultDto = chatMessageRepository.findMessageById(message.getId());
        Optional<MemberResponseDTO> temp = memberRepository.findMemberById(messageQueryResultDto.getSenderId());
        String senderName = "";
        if(temp.isPresent()) {
            senderName = (messageQueryResultDto.getSenderId() == 0) ? "" : temp.get().getName();
        }


        return ChatMessageResponseDto.builder()
                .id(messageQueryResultDto.getId())
                .chatRoomId(messageQueryResultDto.getChatRoomId())
                .senderName(senderName)
                .type(messageQueryResultDto.getType())
                .content(messageQueryResultDto.getContent())
                .fileUrl(messageQueryResultDto.getFileUrl())
                .createdAt(messageQueryResultDto.getCreatedAt())
                .isMine(chatMessageRequestDto.getSenderId() == messageQueryResultDto.getSenderId())
                .readCount(messageQueryResultDto.getReadCount() - 1)
                .senderId(chatMessageRequestDto.getSenderId())
                .build();

    }


    @Override
    public List<ChatMessageResponseDto> getMessagesByChatRoomId(long chatRoomId, long memberId, int page, int size) {
        // 페이지와 사이즈를 레포지토리로 전달
        List<MessageQueryResultDto> messageList = chatMessageRepository.findMessagesByChatRoomId(chatRoomId, page, size);

        return messageList.stream().map((messageQueryResultDto) -> {
            Optional<MemberResponseDTO> temp = memberRepository.findMemberById(messageQueryResultDto.getSenderId());
            String senderName = "";
            if(temp.isPresent()) {
                senderName = (messageQueryResultDto.getSenderId() == 0) ? "" : temp.get().getName();
            }
            return ChatMessageResponseDto.builder()
                    .id(messageQueryResultDto.getId())
                    .chatRoomId(messageQueryResultDto.getChatRoomId())
                    .senderName(senderName) // senderName 필드 채우기
                    .type(messageQueryResultDto.getType())
                    .content(messageQueryResultDto.getContent())
                    .fileUrl(messageQueryResultDto.getFileUrl())
                    .createdAt(messageQueryResultDto.getCreatedAt())
                    .isMine(memberId == messageQueryResultDto.getSenderId()) // isMine 필드 할당
                    .readCount(messageQueryResultDto.getReadCount() - 1)
                    .senderId(messageQueryResultDto.getSenderId())
                    .build();
        }).toList();
    }

    // 본인이 보낸 메시지 삭제
    @Override
    public int deleteMessagesById(long memberId, List<Long> messageIdList) {
        return 0;
    }

    /**
     * 메시지를 삭제 처리하고, 업데이트된 메시지 정보를 반환합니다.
     * @param memberId 삭제를 요청한 사용자 ID
     * @param messageId 삭제할 메시지 ID
     * @return 업데이트된 메시지 정보 DTO
     */
    @Transactional
    public ChatMessageResponseDto deleteMessage(long memberId, long messageId) {
        // 1. 메시지 정보를 가져와서 보낸 사람 확인
        MessageQueryResultDto message = chatMessageRepository.findMessageById(messageId);
        if (message == null) {
            throw new NotFoundException(INVALID_REQUEST);
        }
        if (message.getSenderId() != memberId) {
            throw new ForbiddenException(INSUFFICIENT_PERMISSION);
        }

        // 2. Soft Delete 실행
        int updatedRows = chatMessageRepository.softDeleteMessage(messageId);
        if (updatedRows == 0) {
            throw new RuntimeException("메시지 삭제에 실패했습니다.");
        }

        // 3. 변경된 메시지 정보를 다시 조회하여 DTO로 반환
        MessageQueryResultDto deletedMessage = chatMessageRepository.findMessageById(messageId);
        return ChatMessageResponseDto.builder()
                .id(deletedMessage.getId())
                .chatRoomId(deletedMessage.getChatRoomId())
                .content(deletedMessage.getContent())
                .type(deletedMessage.getType())
                .createdAt(deletedMessage.getCreatedAt())
                .isMine(memberId == deletedMessage.getSenderId())
                .readCount(deletedMessage.getReadCount() - 1)
                .build();
    }

}