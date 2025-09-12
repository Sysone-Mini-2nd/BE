package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatMessageRepository;
import com.sys.stm.domains.messenger.dao.ChatRoomParticipantRepository;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomParticipantService {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

    public int createChatRoomParticipants(List<ChatRoomParticipant> chatRoomParticipants) {
        int result = 0;
        for (ChatRoomParticipant chatRoomParticipant : chatRoomParticipants) {
            int returnedValue = chatRoomParticipantRepository.createChatRoomParticipant(chatRoomParticipant);
            result += returnedValue;
        }
        return result;
    }

    public int deleteFromChatRoom(long id, long memberId) {
        // 채팅방 참여 인원 테이블에서 해당 사원 삭제
        return chatRoomParticipantRepository.deleteFromChatRoom(id, memberId);
    }

    // 채팅방 입장 시 마지막 메시지 기반으로 읽음 처리
    public void updateLastReadAtByMessageId(long chatRoomId, long memberId, Long lastReadMessageId) {
        // 현재 시간
        Timestamp readAt = Timestamp.valueOf(LocalDateTime.now());

        if (lastReadMessageId != null) {
            // 마지막 메시지 id가 생성된 시간 추출
            Timestamp messageCreatedAt = chatMessageRepository.findCreatedAtByMessageId(lastReadMessageId);
            if (messageCreatedAt != null) {
                readAt = messageCreatedAt;
            }
        }
        chatRoomParticipantRepository.updateLastReadAt(chatRoomId, memberId, readAt);
    }

    public int findMemberByMemberId(Long memberId) {
        return chatRoomParticipantRepository.findMemberByMemberId(memberId);
    }

    public Map<Long, List<String>> findParticipantNamesMapByRoomIds(List<Long> roomIds) {
        List<ParticipantInfoResponseDto> participants =
                chatRoomParticipantRepository.findParticipantsByRoomIds(roomIds);

        // 4. 참여자 정보를 채팅방 ID별로 그룹핑한다. (Map<채팅방ID, List<참여자이름>>)
        return participants.stream()
                .collect(Collectors.groupingBy(
                                ParticipantInfoResponseDto::getChatRoomId,
                                Collectors.mapping(ParticipantInfoResponseDto::getName, Collectors.toList())
                        )
                );
    }

    public void updateLastReadAt(long chatRoomId, long memberId, Timestamp timestamp) {
        chatRoomParticipantRepository.updateLastReadAt(chatRoomId, memberId, timestamp);
    }

    public List<String> findNamesByChatRoomId(long chatRoomId) {

        return chatRoomParticipantRepository.findNamesByChatRoomId(chatRoomId);
    }

    public List<Long> findParticipantIdsByRoomId(long chatRoomId) {
        return chatRoomParticipantRepository.findParticipantIdsByRoomId(chatRoomId);
    }
}


