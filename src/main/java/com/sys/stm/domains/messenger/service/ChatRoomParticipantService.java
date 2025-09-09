package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatRoomParticipantRepository;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomParticipantService {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

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
}
