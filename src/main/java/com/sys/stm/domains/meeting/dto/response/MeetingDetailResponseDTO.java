package com.sys.stm.domains.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sys.stm.domains.meeting.domain.MeetingType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
/** 작성자: 배지원 */
@Data
@Builder
public class MeetingDetailResponseDTO {
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp progressDate;
    private List<MeetingParticipantResponseDTO> participants;
    private String writerName;
    private String place;
    private String content;
    private String type;
    private MeetingAnalysisResponseDTO aiSummary;
}


