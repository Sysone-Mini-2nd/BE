package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberProfileMapper {

    MemberProfileResponseDto findMemberProfileById(@Param("id") long id);

}