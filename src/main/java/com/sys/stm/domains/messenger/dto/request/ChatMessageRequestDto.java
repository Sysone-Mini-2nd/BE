package com.sys.stm.domains.messenger.dto.request;

import com.sys.stm.domains.messenger.domain.Message;
import lombok.*;
/** 작성자: 조윤상 */
@ToString
@Getter
@Builder
public class ChatMessageRequestDto {
    private long chatRoomId;
    private long senderId;
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
