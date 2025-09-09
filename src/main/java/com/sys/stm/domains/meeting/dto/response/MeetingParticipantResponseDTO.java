package com.sys.stm.domains.meeting.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingParticipantResponseDTO {
    private Long id;
    private String name;
}
