package com.sys.stm.domains.issue.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueListRequestDTO {
    private String priority;   // 우선순위
    private Long memberId;   // 담당자
    private String status;     // 상태
    private String search;   // 검색어
}
