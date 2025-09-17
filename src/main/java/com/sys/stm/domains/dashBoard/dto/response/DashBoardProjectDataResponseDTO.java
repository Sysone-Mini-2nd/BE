package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.project.domain.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
/** 작성자: 배지원 */
@Data
@Builder
public class DashBoardProjectDataResponseDTO {
    private Long id;                // 프로젝트 ID
    private String name;            // 프로젝트 이름
    private List<String> participant;  // 참여자
    private Double progressRate;     // 진행률(퍼센트)
    private Integer totalTasks;       // 전체 작업 수
    private Timestamp endDate;      // 프로젝트 마감일
}
