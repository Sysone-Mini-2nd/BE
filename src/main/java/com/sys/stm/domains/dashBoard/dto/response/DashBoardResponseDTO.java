package com.sys.stm.domains.dashBoard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/** 작성자: 배지원 */
@Builder
@Data
public class DashBoardResponseDTO {                     // 대시보드 반환 값
    private String role;
    private DashBoardProjectResponseDTO  projectGraph;
    @Builder.Default
    private List<DashBoardUserIssueTrackingResponseDTO> issuesGraph = new ArrayList<>();
    @Builder.Default
    private DashBoardWeekendIssueResponseDTO weekendIssues = null;
    @Builder.Default
    private DashBoardIssuePriorityResponseDTO  priorities = null;
    @Builder.Default
    private List<DashBoardIssueErrorResponseDTO>  errorPriorities= null;
}
