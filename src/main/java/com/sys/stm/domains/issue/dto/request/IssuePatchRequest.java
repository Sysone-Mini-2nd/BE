package com.sys.stm.domains.issue.dto.request;

import com.sys.stm.domains.issue.domain.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssuePatchRequest {
    private IssueStatus status;
}
