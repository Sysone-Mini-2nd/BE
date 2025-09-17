package com.sys.stm.domains.member.service;
import com.sys.stm.domains.member.dto.request.MemberCreateRequestDTO;
import com.sys.stm.domains.member.dto.request.MemberUpdateRequestDTO;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;

import java.util.List;
/** 작성자: 김대호 */
public interface MemberService {

    // 사용자 등록
    void createMember(MemberCreateRequestDTO memberCreateRequestDTO);

    // 사용자 조회 (ID로)
    MemberResponseDTO getMemberById(Long id);

    // 모든 사용자 조회
    List<MemberResponseDTO> getAllMembers();

    // 사용자 수정
    void updateMember(Long id, MemberUpdateRequestDTO memberUpdateRequestDTO);

    // 사용자 삭제
    void deleteMember(Long id);
}

