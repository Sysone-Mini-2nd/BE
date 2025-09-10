package com.sys.stm.domains.issue.service;

import com.sys.stm.domains.issue.dao.IssueRepository;
import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssuePatchRequestDTO;
import com.sys.stm.domains.issue.dto.request.IssueUpdateRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueListResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponseDTO;
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
    public IssueDetailResponseDTO getIssue(Long issueId) {
        return issueRepository.findById(issueId)
                        .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public IssueListResponseDTO getProjectIssues(Long projectId, IssueListRequestDTO issueListRequestDTO) {
        List<IssueSummaryResponseDTO> allSummary = issueRepository.findAllFilteredSummary(projectId,
                issueListRequestDTO);

        return IssueListResponseDTO.builder()
                .issues(allSummary)
                .build();
    }

    @Override
    @Transactional
    public IssueDetailResponseDTO createProjectIssue(Long projectId, IssueCreateRequestDTO issueCreateRequestDTO) {
        Issue requestIssue = Issue.builder()
                .projectId(projectId)
                .title(issueCreateRequestDTO.getTitle())
                .desc(issueCreateRequestDTO.getDesc())
                .memberId(issueCreateRequestDTO.getMemberId())
                .startDate(issueCreateRequestDTO.getStartDate())
                .endDate(issueCreateRequestDTO.getEndDate())
                .priority(issueCreateRequestDTO.getPriority())
                .status(issueCreateRequestDTO.getStatus())
                .build();

         if(issueRepository.createIssue(requestIssue) == 0){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        for(Long tagId : issueCreateRequestDTO.getTagIds()){
            if(issueTagRepository.insertIssueTag(requestIssue.getId(), tagId) == 0){
                throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
            }
        }

        return issueRepository.findById(requestIssue.getId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    public IssueDetailResponseDTO updateIssueStatus(Long issueId, IssuePatchRequestDTO issuePatchRequestDTO) {
        Issue requestIssue = Issue.builder()
                .id(issueId)
                .status(issuePatchRequestDTO.getStatus())
                .build();

        if(issueRepository.updateIssueStatus(requestIssue) < 1){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }

        return issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.DATA_NOT_FOUND));
    }

    @Override
    @Transactional
    public IssueDetailResponseDTO updateIssue(Long issueId, IssueUpdateRequestDTO issueUpdateRequestDTO) {
        Issue requestIssue = Issue.builder()
                .id(issueId)
                .projectId(issueUpdateRequestDTO.getProjectId())
                .memberId(issueUpdateRequestDTO.getMemberId())
                .title(issueUpdateRequestDTO.getTitle())
                .desc(issueUpdateRequestDTO.getDesc())
                .startDate(issueUpdateRequestDTO.getStartDate())
                .endDate(issueUpdateRequestDTO.getEndDate())
                .status(issueUpdateRequestDTO.getStatus())
                .priority(issueUpdateRequestDTO.getPriority())
                .build();

        if(issueRepository.updateIssue(requestIssue) < 1){
            throw new BadRequestException(ExceptionMessage.INVALID_REQUEST);
        }
        
        List<Long> newTagIds = issueUpdateRequestDTO.getTagIds();
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

    @Override
    public void deleteIssue(Long issueId) {
        if(issueRepository.deleteIssueById(issueId) == 0){
            throw new NotFoundException(ExceptionMessage.DATA_NOT_FOUND);
        }
    }
}
