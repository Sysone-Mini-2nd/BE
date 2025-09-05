package com.sys.stm.domains.issueTag.dao;

import com.sys.stm.domains.tag.domain.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IssueTagRepository {
    List<Tag> findAllByIssueId(Long issueId);

    int insertIssueTag(@Param("issueId") Long issueId,
                        @Param("tagId") Long tagId);
    int deleteIssueTag(Long tagId);
}
