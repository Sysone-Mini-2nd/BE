package com.sys.stm.domains.dashBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
public class DashBoardUserResponseDTO {
    private int projectCount;
    private int issueCount;
    private List<DashBoardProjectResponseDTO>  projects;
    private List<DashBoardIssueTrackingResponseDTO> issues;
    private List<DashBoardWeekendIssueResponseDTO> weekendIssues;
    private List<DashBoardIssuePriorityResponseDTO>  priorities;


}
