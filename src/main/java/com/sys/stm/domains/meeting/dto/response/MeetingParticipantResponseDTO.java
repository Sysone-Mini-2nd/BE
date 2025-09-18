package com.sys.stm.domains.meeting.dto.response;

import lombok.Builder;
import lombok.Data;
/** 작성자: 배지원 */
@Data
@Builder
public class MeetingParticipantResponseDTO {
    private Long id;
    private String name;
}
