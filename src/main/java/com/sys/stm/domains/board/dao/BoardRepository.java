package com.sys.stm.domains.board.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;
/** 작성자: 배지원 */
@Mapper
public interface BoardRepository {
    Optional<Integer> getDeptCount();
}
