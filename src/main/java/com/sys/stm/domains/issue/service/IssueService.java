package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;

import java.util.List;
/** 작성자: 백승준 */
public interface IssueService {
    IssueDetailResponseDTO getIssue(Long issueId);

    IssueListResponseDTO getProjectIssues(Long projectId, IssueListRequestDTO issueListRequestDTO);

    IssueDetailResponseDTO createProjectIssue(Long projectId, IssueCreateRequestDTO issueCreateRequestDTO);

    IssueDetailResponseDTO updateIssueStatus(Long issueId, IssuePatchRequestDTO issuePatchRequestDTO);

    IssueDetailResponseDTO updateIssue(Long issueId, IssueUpdateRequestDTO issueUpdateRequestDTO);

    void deleteIssue(Long issueId);

    int countIssuesByProjectIdsAndEndDateWithinWeek(List<Long> projectIds);

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndMemberId(Long projectId, Long memberId);
}
