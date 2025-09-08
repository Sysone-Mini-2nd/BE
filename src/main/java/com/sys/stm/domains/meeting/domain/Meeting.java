package com.sys.stm.domains.meeting.domain;

import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Meeting extends BaseEntity {

    private Long id;                    // 회의록 ID (PK)
    private Long projectId;             // 프로젝트 ID (FK)
    private String title;               // 회의 제목
    private String content;             // 회의 내용
    private Long memberId;              // 회의록 작성자 (FK)
    private String type;                // 회의 종류
    private Timestamp progressDate;     // 회의 진행시간
    private String place;               // 회의 장소


    // Participant와의 관계 (1:N)
    private List<MeetingParticipant> participants;

    // 생성자에서 BaseEntity 초기화
    public Meeting(MeetingCreateRequestDTO request, Long projectId, Long memberId, String content) {
        this.projectId = projectId;
        this.title = request.getTitle();
        this.content = content;
        this.memberId = memberId;
        this.type = request.getType().toString();
        this.progressDate = request.getProgressDate();
        this.place = request.getPlace();
    }
}
