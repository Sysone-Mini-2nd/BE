package com.sys.stm.domains.tag.dao;

import com.sys.stm.domains.tag.domain.Tag;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagRepository {
    List<Tag> findTags();
    int insertTag(String name);
    int deleteTag(Long tagId);
    int updateTag(Tag tag);
}
