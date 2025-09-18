package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.sql.Timestamp;
/** 작성자: 조윤상 */
@Mapper
public interface ChatRoomParticipantRepository {

    int createChatRoomParticipant(ChatRoomParticipant chatRoomParticipant);

    int findMemberByMemberId(@Param("memberId") long memberId);

    int deleteFromChatRoom(@Param("id") long id, @Param("memberId") long memberId);

    List<ParticipantInfoResponseDto> findParticipantsByRoomIds(@Param("roomIds") List<Long> roomIds);

    int updateLastReadAt(@Param("chatRoomId") long chatRoomId, @Param("memberId") long memberId, @Param("readAt") Timestamp readAt);

    List<String> findNamesByChatRoomId(@Param("chatRoomId") long chatRoomId);

    List<Long> findParticipantIdsByRoomId(long chatRoomId);

    Long existChatRoom(@Param("senderId") long senderId,@Param("readerId") long readerId);
}
