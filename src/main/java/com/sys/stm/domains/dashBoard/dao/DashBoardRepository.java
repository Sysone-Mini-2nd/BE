package com.sys.stm.domains.dashBoard.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface DashBoardRepository {
    Optional<Integer> getDeptCount();
}
