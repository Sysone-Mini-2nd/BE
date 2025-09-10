package com.sys.stm.domains.issue.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class Issue extends BaseEntity {
    private Long id;
    private Long projectId;
    private Long memberId;
    private String title;
    private String desc;
    private Timestamp startDate;
    private Timestamp endDate;
    private IssueStatus status;
    private IssuePriority priority;
}
