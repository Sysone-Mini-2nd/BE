package com.sys.stm.domains.messenger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/** 작성자: 조윤상 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantInfoResponseDto {

    private Long chatRoomId;
    private String name;

}
