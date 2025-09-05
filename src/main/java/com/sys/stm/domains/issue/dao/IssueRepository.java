package com.sys.stm.domains.issue.dao;

import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponse;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueRepository {
    Optional<IssueDetailResponse> findById(Long id);
    List<IssueSummaryResponse> findAllFilteredSummary(IssueListRequest issueListRequest);
    int createIssue(Issue issue); // 실패 0 성공 1
    Long findLastInsertedId();
}
