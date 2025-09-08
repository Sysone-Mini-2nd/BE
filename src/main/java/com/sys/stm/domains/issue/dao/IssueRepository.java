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
    int createIssue(Issue issue);
    int updateIssueStatus(Issue issue);
    int updateIssue(Issue issue);
}
