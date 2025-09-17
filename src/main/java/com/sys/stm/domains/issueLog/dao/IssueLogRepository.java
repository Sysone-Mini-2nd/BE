package com.sys.stm.domains.issueLog.dao;

import com.sys.stm.domains.issueLog.domain.IssueLog;
import com.sys.stm.domains.issueTag.domain.IssueTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
/** 작성자: 배지원 */
@Mapper
public interface IssueLogRepository {

    void createIssueLog(@Param("issueLog") IssueLog issueLog);

    Optional<IssueLog> findByIssueId(@Param("issueId") Long issueId);

    void deleteIssueLog(IssueLog issueLog);
}
