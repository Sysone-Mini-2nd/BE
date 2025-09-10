package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.issue.domain.IssuePriority;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DashBoardIssuePriorityResponseDTO {
    long issueId;
    String title;
    String writerName;
    IssuePriority priority;
}
