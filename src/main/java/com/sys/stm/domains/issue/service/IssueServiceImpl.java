package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dao.IssueRepository;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponse;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;

    @Override
    public IssueDetailResponse getIssue(Long issueId) {
        return issueRepository.findById(issueId)
                        .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public IssueListResponse getProjectIssues(Long projectId, String priority, Long assigneeId, String status) {
        IssueListRequest issueListRequest = IssueListRequest.builder()
                .projectId(projectId)
                .priority(priority)
                .assigneeId(assigneeId)
                .status(status)
                .build();

        List<IssueSummaryResponse> allSummary = issueRepository.findAllFilteredSummary(issueListRequest);

        return IssueListResponse.builder()
                .issues(allSummary)
                .build();
    }
}
