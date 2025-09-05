package com.sys.stm.domains.issueTag.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IssueTagRepository {
    int insertIssueTag(@Param("issueId") Long issueId,
                        @Param("tagId") Long tagId);
    int deleteIssueTag(@Param("issueId") Long issueId,
                        @Param("tagId") Long tagId);
    int updateIssueTag(@Param("issueId") Long issueId,
                        @Param("prevTagId") Long prevTagId,
                        @Param("nextTagId") Long nextTagId);
    int deleteIssueTagByIssueId(@Param("issueId") Long issueId);
}
