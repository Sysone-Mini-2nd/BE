package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.sql.Timestamp;

@Mapper
public interface ChatRoomParticipantRepository {

    // TODO: 기본키 오류나서 못하고 있음. ChatRoomParticipant를 하나씩 넣는것보다 List로 sql문 한번만 보내도록 리팩토링 필요..
    // int createChatRoomParticipants(@Param("participantList") List<ChatRoomParticipant> chatRoomParticipants);
    int createChatRoomParticipant(ChatRoomParticipant chatRoomParticipant);

    int findMemberByMemberId(@Param("memberId") long memberId);

    int deleteFromChatRoom(@Param("id") long id, @Param("memberId") long memberId);

    List<ParticipantInfoResponseDto> findParticipantsByRoomIds(@Param("roomIds") List<Long> roomIds);

    int updateLastReadAt(@Param("chatRoomId") long chatRoomId, @Param("memberId") long memberId, @Param("readAt") Timestamp readAt);

    List<String> findNamesByChatRoomId(@Param("chatRoomId") long chatRoomId);
}
