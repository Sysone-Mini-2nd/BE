package com.sys.stm.domains.issueLog.service;

import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;
import com.sys.stm.domains.issueLog.domain.IssueLog;

import java.util.List;
/** 작성자: 배지원 */
public interface IssueLogService {
    void createIssueLog(IssueLog issueLog);

    IssueLog findByIssueId(Long issueId);

    void deleteIssueLog(IssueLog issueLog);
}
