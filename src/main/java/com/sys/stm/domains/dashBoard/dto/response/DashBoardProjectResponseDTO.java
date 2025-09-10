package com.sys.stm.domains.dashBoard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class DashBoardProjectResponseDTO {
    Long projectId;
    double todo;                // 앞으로 할 일
    double progress;            // 진행중
    double done;                // 완료
}


