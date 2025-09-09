package com.sys.stm.domains.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MeetingListResponseDTO {
    private String title;
    private String writerName;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp progressTime;
    private String place;
    private int attendeeCount;
}
