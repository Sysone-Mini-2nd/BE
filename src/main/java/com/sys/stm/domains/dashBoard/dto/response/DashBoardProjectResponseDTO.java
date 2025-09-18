package com.sys.stm.domains.dashBoard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
/** 작성자: 배지원 */
@Data
@Builder
public class DashBoardProjectResponseDTO {                  // 프로젝트 그래프
    private Long id;
    private Integer total;
    private Integer todo;                // 앞으로 할 일
    private Integer progress;            // 진행중
    private Integer review;              // 리뷰중
    private Integer done;                // 완료
}


