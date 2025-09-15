package com.sys.stm.domains.messenger.dto.response;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageQueryResultDto {
    private Long id;
    private Long senderId;
    private Long chatRoomId;
    private String type;
    private String content;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private long readCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}