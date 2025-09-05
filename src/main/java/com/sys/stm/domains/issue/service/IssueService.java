package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;

public interface IssueService {
    IssueDetailResponse getIssue(Long issueId);
    IssueListResponse getProjectIssues(Long projectId, String priority, Long assigneeId, String status);
}
