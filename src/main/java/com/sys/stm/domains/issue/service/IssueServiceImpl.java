package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dao.IssueRepository;
import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequest;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequest;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueListResponse;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponse;
import com.sys.stm.domains.issueTag.dao.IssueTagRepository;
import com.sys.stm.domains.issueTag.domain.IssueTag;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final IssueTagRepository issueTagRepository;

    @Override
    public IssueDetailResponse getIssue(Long issueId) {
        return issueRepository.findById(issueId)
                        .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public IssueListResponse getProjectIssues(Long projectId, IssueListRequest issueListRequest) {
        List<IssueSummaryResponse> allSummary = issueRepository.findAllFilteredSummary(projectId, issueListRequest);

        return IssueListResponse.builder()
                .issues(allSummary)
                .build();
    }

    @Override
    @Transactional
    public IssueDetailResponse createProjectIssue(Long projectId, IssueCreateRequest issueCreateRequest) {
        Issue requestIssue = Issue.builder()
                .projectId(projectId)
                .title(issueCreateRequest.getTitle())
                .desc(issueCreateRequest.getDesc())
                .memberId(issueCreateRequest.getMemberId())
                .startDate(issueCreateRequest.getStartDate())
                .endDate(issueCreateRequest.getEndDate())
                .priority(issueCreateRequest.getPriority())
                .status(issueCreateRequest.getStatus())
                .build();

         if(issueRepository.createIssue(requestIssue) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        for(Long tagId : issueCreateRequest.getTagIds()){
            if(issueTagRepository.insertIssueTag(requestIssue.getId(), tagId) == 0){
                throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
            }
        }

        return issueRepository.findById(requestIssue.getId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public IssueDetailResponse updateIssueStatus(Long issueId, IssuePatchRequest issuePatchRequest) {
        Issue requestIssue = Issue.builder()
                .id(issueId)
                .status(issuePatchRequest.getStatus())
                .build();

        if(issueRepository.updateIssueStatus(requestIssue) < 1){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    @Transactional
    public IssueDetailResponse updateIssue(Long issueId, IssueUpdateRequest issueUpdateRequest) {
        Issue requestIssue = Issue.builder()
                .id(issueId)
                .projectId(issueUpdateRequest.getProjectId())
                .memberId(issueUpdateRequest.getMemberId())
                .title(issueUpdateRequest.getTitle())
                .desc(issueUpdateRequest.getDesc())
                .startDate(issueUpdateRequest.getStartDate())
                .endDate(issueUpdateRequest.getEndDate())
                .status(issueUpdateRequest.getStatus())
                .priority(issueUpdateRequest.getPriority())
                .build();

        if(issueRepository.updateIssue(requestIssue) < 1){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }
        
        List<Long> newTagIds = issueUpdateRequest.getTagIds();
        if (newTagIds == null) {
            newTagIds = new java.util.ArrayList<>();
        }
        List<IssueTag> prevIssueTags = issueTagRepository.findAllByIssueId(issueId);

        List<Long> prevTagIds = prevIssueTags.stream()
                .map(IssueTag::getTagId)
                .toList();

        // 추가할 태그
        for (Long tagId : newTagIds) {
            if (!prevTagIds.contains(tagId)) {
                if (issueTagRepository.insertIssueTag(issueId, tagId) == 0) {
                    throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
                }
            }
        }

        for (IssueTag prevIssueTag : prevIssueTags) {
            if (!newTagIds.contains(prevIssueTag.getTagId())) {
                if (issueTagRepository.deleteIssueTagById(prevIssueTag.getId()) == 0) {
                    throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
                }
            }
        }

        return issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }
}
