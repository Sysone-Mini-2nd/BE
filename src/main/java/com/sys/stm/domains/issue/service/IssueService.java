package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequest;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequest;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;

public interface IssueService {
    IssueDetailResponse getIssue(Long issueId);
    IssueListResponse getProjectIssues(Long projectId, IssueListRequest issueListRequest);
    IssueDetailResponse createProjectIssue(Long projectId, IssueCreateRequest issueCreateRequest);
    IssueDetailResponse updateIssueStatus(Long issueId, IssuePatchRequest issuePatchRequest);
    IssueDetailResponse updateIssue(Long issueId, IssueUpdateRequest issueUpdateRequest);
}
