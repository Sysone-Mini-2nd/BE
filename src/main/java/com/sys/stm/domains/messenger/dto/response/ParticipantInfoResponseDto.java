package com.sys.stm.domains.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantInfoResponseDto {

    private Long chatRoomId;
    private String name;

}
