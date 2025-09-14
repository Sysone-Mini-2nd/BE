package com.sys.stm.domains.member.dao;

import com.sys.stm.domains.member.domain.Member;
import com.sys.stm.domains.member.dto.request.MemberCreateRequestDTO;
import com.sys.stm.domains.member.dto.request.MemberUpdateRequestDTO;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberRepository {

    // 사용자 등록
    int insertMember(MemberCreateRequestDTO memberCreateRequestDTO);

    // 사용자 조회 (ID로)
    Optional<MemberResponseDTO> findMemberById(Long id);

    // 모든 사용자 조회 (삭제되지 않은 사용자만)
    List<MemberResponseDTO> findAllMembers();

    // 사용자 수정
    int updateMember(@Param("id") Long id, @Param("dto") MemberUpdateRequestDTO memberUpdateRequestDTO);

    // 사용자 삭제 (논리 삭제)
    int deleteMember(Long id);

    // account_id 중복 체크용
    boolean existsByAccountId(String accountId);

    // 로그인용 - account_id로 사용자 찾기
    Optional<Member> findByAccountId(String accountId);

    // 마지막 로그인 시간 업데이트 (인증용)
    int updateLastLoginTime(@Param("id") Long id, @Param("lastLoginAt") Timestamp lastLoginAt);
}
