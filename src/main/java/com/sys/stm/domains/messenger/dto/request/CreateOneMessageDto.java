package com.sys.stm.domains.messenger.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class CreateOneMessageDto {
    private long senderId;
    private long readerId;
    private String content;
}
