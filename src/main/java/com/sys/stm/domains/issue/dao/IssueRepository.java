package com.sys.stm.domains.issue.dao;

import com.sys.stm.domains.issue.domain.Issue;
import com.sys.stm.domains.issue.dto.request.IssueListRequestDTO;
import com.sys.stm.domains.issue.dto.response.IssueDetailResponseDTO;
import com.sys.stm.domains.issue.dto.response.IssueSummaryResponseDTO;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IssueRepository {
    Optional<IssueDetailResponseDTO> findById(Long id);
    List<IssueSummaryResponseDTO> findAllFilteredSummary(@Param("projectId") Long projectId, @Param("issueListRequestDTO") IssueListRequestDTO issueListRequestDTO);
    List<Issue> findAllByProjectIdAndMemberId(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
    List<Issue> findAllByMemberId(@Param("memberId") Long memberId);
    List<Issue> findAllByProjectId(Long projectId);
    void unassignIssues(@Param("issueIds") List<Long> issueIds);
    void deleteIssuesByIds(List<Long> issueIds);
    int createIssue(Issue issue);
    int updateIssueStatus(Issue issue);
    int updateIssue(Issue issue);
    int deleteIssueById(Long issueId);
}
