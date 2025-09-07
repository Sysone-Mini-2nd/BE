package com.sys.stm.domains.issue.dao;

import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueListRequest;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponse;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponse;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IssueRepository {
    Optional<IssueDetailResponse> findById(Long id);
    List<IssueSummaryResponse> findAllFilteredSummary(@Param("projectId") Long projectId, @Param("issueListRequest") IssueListRequest issueListRequest);
    int createIssue(Issue issue);
    int updateIssueStatus(Issue issue);
    int updateIssue(Issue issue);
}
