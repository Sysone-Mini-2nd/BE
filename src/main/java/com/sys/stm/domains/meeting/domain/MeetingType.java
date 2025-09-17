package com.sys.stm.domains.meeting.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/** 작성자: 배지원 */
@Getter
@RequiredArgsConstructor
public enum MeetingType {
    SCRUM("스크럼"),
    MEETING("미팅"),
    REVIEW("리뷰"),
    RETROSPECTIVE("회고");

    private final String description;
}
