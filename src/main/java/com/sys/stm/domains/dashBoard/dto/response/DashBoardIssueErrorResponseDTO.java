package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.issue.domain.IssuePriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class DashBoardIssueErrorResponseDTO {                // 우선순위 작업
    private long id;
    private String title;
    private String writerName;
    private long memberId;
    private Timestamp endDate;
}
