package com.sys.stm.domains.issue.dto.request;

import com.sys.stm.domains.issue.domain.IssueStatus;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuePatchRequestDTO {
    private IssueStatus status;
    private Timestamp startDate;
    private Timestamp endDate;
}
