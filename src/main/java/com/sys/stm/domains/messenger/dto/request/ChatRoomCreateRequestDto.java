package com.sys.stm.domains.messenger.dto.request;

import com.sys.stm.domains.messenger.domain.ChatRoom;
import com.sys.stm.domains.messenger.domain.ChatRoomParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateRequestDto {
    // 채팅방 이름
    private String name;
    // 채팅방 타입 (Group, One_on_One)
    private String type;
    // 채팅방 참여 인원 id 리스트
    private List<Long> memberIdList;


    public ChatRoom toChatRoom(){
        return ChatRoom.builder()
                .name(this.name)
                .type(this.type)
                .build();
    }

    public List<ChatRoomParticipant> toChatRoomParticipants(long chatRoomId){
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        return memberIdList.stream()
                .map((id) -> {
                    return ChatRoomParticipant.builder()
                            .chatRoomId(chatRoomId)
                            .memberId(id)
                            .lastReadAt(time)
                            .build();
                })
                .toList();

    }
}
