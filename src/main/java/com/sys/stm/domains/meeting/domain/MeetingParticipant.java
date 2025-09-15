package com.sys.stm.domains.meeting.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MeetingParticipant extends BaseEntity {

    private Long id;                    // 참석자 ID (PK)
    private Long meetingId;             // 회의 ID (FK)
    private Long memberId;              // 회원 ID (FK)

    // 생성자
    public MeetingParticipant(Long meetingId, Long memberId) {
        this.meetingId = meetingId;
        this.memberId = memberId;
    }
}