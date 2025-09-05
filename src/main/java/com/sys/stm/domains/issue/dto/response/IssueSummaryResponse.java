package com.sys.stm.domains.issue.dto.response;

import com.sys.stm.domains.issue.domain.IssuePriority;
import com.sys.stm.domains.issue.domain.IssueStatus;
import com.sys.stm.domains.tag.domain.Tag;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueSummaryResponse {
    private Long id; // 이슈 PK
    private String title; // 이슈 제목
    private String desc; // 이슈 설명
    private String memberName; // 담당자 이름
    private Long memberId; // 담당자 pk
    private IssueStatus status; // T0DO, IN_PROGRESS, REVIEW, DONE
    private IssuePriority priority; // LOW, NORMAL, HIGH
    private Timestamp startDate;
    private Timestamp endDate;
    private List<Tag> tags; // tag 목록
    private Integer dDay; // 남은 기간 && 초과된 기간
}
