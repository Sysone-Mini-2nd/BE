package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.ChatRoomMapper;
import com.sys.stm.domains.messenger.dao.ChatRoomParticipantMapper;
import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomParticipantService {

    private final ChatRoomParticipantMapper chatRoomParticipantMapper;

    public int createChatRoomParticipants(List<ChatRoomParticipant> chatRoomParticipants){
        int result = 0;
        for(ChatRoomParticipant chatRoomParticipant: chatRoomParticipants){
            int returnedValue = chatRoomParticipantMapper.createChatRoomParticipant(chatRoomParticipant);
            result += returnedValue;
        }
        return result;
    }

}
