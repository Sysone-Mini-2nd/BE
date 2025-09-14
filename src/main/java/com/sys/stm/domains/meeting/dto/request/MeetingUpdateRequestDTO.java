package com.sys.stm.domains.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sys.stm.domains.meeting.domain.MeetingType;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class MeetingUpdateRequestDTO {
    private String title;           // 회의록 제목
    private String content;         // 회의록 내용
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private MeetingType type;            // 회의 타입 (SCRUM, MEETING, REVIEW, RETROSPECTIVE)
    private String place;           // 회의 장소
    private List<Long> participantIds;
}
