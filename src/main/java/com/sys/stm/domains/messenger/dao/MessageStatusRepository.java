package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.MessageStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/** 작성자: 조윤상 */
@Mapper
public interface MessageStatusRepository {
    List<Long> findReadMessageIdsByChatRoomIdAndReaderId(@Param("chatRoomId") long chatRoomId, @Param("readerId") long readerId);
    int createMessageStatus (@Param("messageStatus") MessageStatus messageStatus);
}
