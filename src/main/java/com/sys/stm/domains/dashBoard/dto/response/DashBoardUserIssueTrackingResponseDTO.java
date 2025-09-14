package com.sys.stm.domains.dashBoard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashBoardUserIssueTrackingResponseDTO {                // 이슈 그래프
    private Long id;           // 이슈 ID / 유저 ID
    private String name;      // 이슈 제목 / 유저 이름
    private Integer allottedPeriod;     // 할당된 일수
    private Integer completedPeriod;    // 실제 작업할 일 수
}
