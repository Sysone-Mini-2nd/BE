package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;

public interface IssueService {
    IssueDetailResponseDTO getIssue(Long issueId);
    IssueListResponseDTO getProjectIssues(Long projectId, IssueListRequestDTO issueListRequestDTO);
    IssueDetailResponseDTO createProjectIssue(Long projectId, IssueCreateRequestDTO issueCreateRequestDTO);
    IssueDetailResponseDTO updateIssueStatus(Long issueId, IssuePatchRequestDTO issuePatchRequestDTO);
    IssueDetailResponseDTO updateIssue(Long issueId, IssueUpdateRequestDTO issueUpdateRequestDTO);
    void deleteIssue(Long issueId);
}
