package com.sys.stm.domains.meeting.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface BoardRepository {
    Optional<Integer> getDeptCount();
}
