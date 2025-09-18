package com.sys.stm.domains.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Timestamp;
/** 작성자: 배지원 */
@Data
@Builder
public class MeetingListResponseDTO {
    private Long id;
    private String title;
    private String writerName;
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp progressTime;
    private String place;
    private int attendeeCount;
}
