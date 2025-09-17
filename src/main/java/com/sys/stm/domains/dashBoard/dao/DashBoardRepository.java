package com.sys.stm.domains.dashBoard.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;
/** 작성자: 배지원 */
@Mapper
public interface DashBoardRepository {
    Optional<Integer> getDeptCount();
}
