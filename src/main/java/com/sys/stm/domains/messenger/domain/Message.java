package com.sys.stm.domains.messenger.domain;

import com.sys.stm.domains.messenger.dto.response.ChatMessageResponseDto;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/** 작성자: 조윤상 */
@Getter
@Setter
@Builder
public class Message extends BaseEntity {

    private Long id;
    private Long senderId;
    private Long chatRoomId;
    private String type;
    private String content;
    private String fileUrl;
    private String fileName;
    private Long fileSize;

    public ChatMessageResponseDto toChatMessageResponseDto(long memberId) {
        return ChatMessageResponseDto.builder()
                .id(id)
                .chatRoomId(chatRoomId)
                .content(content)
                .fileUrl(fileUrl)
                .type(type)
                .createdAt(getCreatedAt().toLocalDateTime())
                .isMine(memberId == senderId)
                .build();
    }
}
