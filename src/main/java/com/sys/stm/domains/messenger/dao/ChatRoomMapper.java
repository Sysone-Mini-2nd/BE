package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.dto.response.ChatRoomInfoResponseDto;
import com.sys.stm.domains.messenger.dto.response.ParticipantInfoResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomMapper {

    List<ChatRoomInfoResponseDto> findChatRoomsByMemberId(@Param("memberId") Long memberId);

    List<ParticipantInfoResponseDto> findParticipantsByRoomIds(@Param("roomIds") List<Long> roomIds);
}

