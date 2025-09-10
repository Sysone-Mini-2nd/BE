package com.sys.stm.domains.dashBoard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class DashBoardIssueTrackingResponseDTO {
    Long memberId;          // 유저 ID
    Long projectId;         // 프로젝트 ID
    Long issueId;           // 이슈 ID
    int allottedPeriod;     // 할당된 일수
    int completedPeriod;    // 실제 작업할 일 수
}
