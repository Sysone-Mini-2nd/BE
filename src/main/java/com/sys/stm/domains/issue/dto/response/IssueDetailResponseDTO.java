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
public class IssueDetailResponseDTO {
    private Long id;
    private Long projectId;
    private Long memberId;
    private String title;
    private String desc;
    private Timestamp startDate;
    private Timestamp endDate;
    private IssueStatus status;
    private IssuePriority priority;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Tag> tags;
}
