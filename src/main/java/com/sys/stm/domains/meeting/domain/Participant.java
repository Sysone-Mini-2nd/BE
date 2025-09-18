package com.sys.stm.domains.meeting.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.*;
/** 작성자: 배지원 */
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
