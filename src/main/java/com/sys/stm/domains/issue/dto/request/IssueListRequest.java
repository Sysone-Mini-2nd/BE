package com.sys.stm.domains.issue.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueListRequest {
    private Long projectId;    // 프로젝트 ID
    private String priority;   // 우선순위
    private Long assigneeId;   // 담당자
    private String status;     // 상태
}
