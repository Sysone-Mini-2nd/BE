package com.sys.stm.domains.meeting.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Participant extends BaseEntity {
    private Long id;                    // 참석자 ID (PK)
    private Long meetingId;             // 회의 ID (FK)
    private Long memberId;              // 회원 ID (FK)

    // 생성자
    public Participant(Long meetingId, Long memberId) {
        this.meetingId = meetingId;
        this.memberId = memberId;
    }
}
