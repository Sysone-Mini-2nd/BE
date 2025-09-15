package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.dto.request.ChatRoomUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.ChatRoomInfoResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomRepository {

    List<ChatRoomInfoResponseDto> findChatRoomsByMemberId(@Param("memberId") Long memberId);

    int createChatRoom(ChatRoom chatRoom);

    int updateChatRoom(@Param("id") long id, @Param("dto") ChatRoomUpdateRequestDto dto);

    int updateRecentMessage (@Param("chatRoomId") long chatRoomId, @Param("msg") String message);
}

