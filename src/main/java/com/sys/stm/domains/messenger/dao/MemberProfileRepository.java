package com.sys.stm.domains.messenger.dao;

import com.sys.stm.domains.messenger.dto.request.MemberProfileUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/** 작성자: 조윤상 */
@Mapper
public interface MemberProfileRepository {

    MemberProfileResponseDto findMemberProfileById(@Param("id") long id);

    List<MemberProfileResponseDto> findAllMemberProfiles();

    List<MemberProfileResponseDto> findMemberProfilesByEmailOrName(@Param("keyword") String keyword);

    int updateMemberProfile(@Param("id") long id, @Param("dto") MemberProfileUpdateRequestDto memberProfileUpdateRequestDto);
}