package com.sys.stm.domains.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/** 작성자: 배지원 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingTemplateDataDTO {
    private String meetingTitle;
    private String meetingDate;
    private String meetingTime;
    private String meetingPlace;
    private String writerName;
    private String meetingContent;
    private List<String> mainTopics;
    private List<String> decisions;
    private List<String> priorities;
    private List<ParticipantTemplateData> participants;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParticipantTemplateData {
        private String name;
    }
}