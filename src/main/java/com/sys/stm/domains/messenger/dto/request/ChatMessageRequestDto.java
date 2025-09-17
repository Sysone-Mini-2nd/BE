package com.sys.stm.domains.messenger.dto.request;

import com.sys.stm.domains.messenger.domain.Message;
import lombok.*;
/** 작성자: 조윤상 */
@ToString
@Getter
@Builder
public class ChatMessageRequestDto {
    private long chatRoomId;

    // TODO 인증로직에 따라 송신자 id는 제외할 수도 있음
    private long senderId;

    // TODO 나중에 enum으로 변환
    private String type;
    private String content;
    private String fileUrl;
    private String fileName;
    private long fileSize;


    public Message toMessage(){
        return Message.builder()
                .senderId(senderId)
                .chatRoomId(chatRoomId)
                .type(type)
                .content(content)
                .fileUrl(fileUrl)
                .fileName(fileName)
                .fileSize(fileSize)
                .build();
    }
}
