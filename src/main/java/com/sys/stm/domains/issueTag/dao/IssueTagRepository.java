package com.sys.stm.domains.issueTag.dao;

import com.sys.stm.domains.issueTag.domain.IssueTag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/** 작성자: 백승준 */
@Mapper
public interface IssueTagRepository {
    List<IssueTag> findAllByIssueId(Long issueId);

    int insertIssueTag(@Param("issueId") Long issueId,
                        @Param("tagId") Long tagId);
    int deleteIssueTagById(Long tagId);
}
