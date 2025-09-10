package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMessageRepository {
    List<Message> getMessagesByChatRoomId(@Param("id") long id, @Param("page") int page, @Param("size") int size);

    int createMessage(Message message);
}
