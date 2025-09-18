package com.sys.stm.domains.issue.dto.request;

import com.sys.stm.domains.issue.domain.IssuePriority;
import com.sys.stm.domains.issue.domain.IssueStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueCreateRequestDTO {
    private String title; // 제목
    private String desc; // 설명
    private Long memberId; // 담당자 (pk)
    private List<Long> tagIds = new ArrayList<>(); // 태그(name) 리스트
    private Timestamp startDate; // 시작일
    private Timestamp endDate; // 종료일
    private IssuePriority priority; // 우선순위
    private IssueStatus status; // 상태
}
