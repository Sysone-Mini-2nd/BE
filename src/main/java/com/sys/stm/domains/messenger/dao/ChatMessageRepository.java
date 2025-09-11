package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.Message;
import com.sys.stm.domains.messenger.dto.response.MessageQueryResultDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ChatMessageRepository {
    List<MessageQueryResultDto> findMessagesByChatRoomId(@Param("chatRoomId") long id, @Param("page") int page, @Param("size") int size);

    int createMessage(Message message);

    Timestamp findCreatedAtByMessageId(@Param("messageId") long messageId);

    List<Long> findAllMessageIdsByChatRoomIdAndMessageId(@Param("chatRoomId") long chatRoomId, @Param("messageId") long lastReadMessageId);

    MessageQueryResultDto findMessageById(@Param("messageId") long id);
}

