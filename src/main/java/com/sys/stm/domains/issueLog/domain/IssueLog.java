package com.sys.stm.domains.issueLog.domain;

import com.sys.stm.domains.issue.domain.IssueStatus;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
/** 작성자: 배지원 */
@Data
@Builder
public class IssueLog {
    private Long id;
    private Long issueId;
    private IssueStatus issueStatus;
    private Timestamp startDate;
    private Timestamp endDate;
}
