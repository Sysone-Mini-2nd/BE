package com.sys.stm.domains.issueLog.service;

import com.sys.stm.domains.issue.dao.IssueRepository;
import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponseDTO;
import com.sys.stm.domains.issueLog.dao.IssueLogRepository;
import com.sys.stm.domains.issueLog.domain.IssueLog;
import com.sys.stm.domains.issueTag.dao.IssueTagRepository;
import com.sys.stm.domains.issueTag.domain.IssueTag;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueLogServiceImpl implements IssueLogService {
    private final IssueLogRepository issueLogRepository;

    @Override
    public void createIssueLog(IssueLog issueLog) {
        issueLogRepository.createIssueLog(issueLog);
    }

    @Override
    public IssueLog findByIssueId(Long issueId) {
        return issueLogRepository.findByIssueId(issueId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.ISSUE_LOG_NOT_FOUND));
    }

}
