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
public class IssueUpdateRequestDTO {
    private Long projectId;
    private Long memberId;
    private String title;
    private String desc;
    private Timestamp startDate;
    private Timestamp endDate;
    private IssueStatus status;
    private IssuePriority priority;
    private List<Long> tagIds = new ArrayList<>();
}
