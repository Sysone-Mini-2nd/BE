package com.sys.stm.domains.member.service;

import com.sys.stm.domains.member.dao.MemberRepository;
import com.sys.stm.domains.member.dto.request.MemberCreateRequestDTO;
import com.sys.stm.domains.member.dto.request.MemberUpdateRequestDTO;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/** 작성자: 김대호 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void createMember(MemberCreateRequestDTO memberCreateRequestDTO) {
        // account_id 중복 체크
        if (memberRepository.existsByAccountId(memberCreateRequestDTO.getAccountId())) {
            throw new BadRequestException(ExceptionMessage.DUPLICATE_ACCOUNT_ID);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberCreateRequestDTO.getPassword());
        memberCreateRequestDTO.setPassword(encodedPassword);

        int result = memberRepository.insertMember(memberCreateRequestDTO);
        if (result == 0) {
            throw new BadRequestException(ExceptionMessage.MEMBER_CREATE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberById(Long id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.findAllMembers();
    }

    @Override
    public void updateMember(Long id, MemberUpdateRequestDTO memberUpdateRequestDTO) {
        // 사용자 존재 확인
        memberRepository.findMemberById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND));

        // 비밀번호가 있으면 암호화
        if (memberUpdateRequestDTO.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(memberUpdateRequestDTO.getPassword());
            memberUpdateRequestDTO.setPassword(encodedPassword);
        }

        int result = memberRepository.updateMember(id, memberUpdateRequestDTO);
        if (result == 0) {
            throw new BadRequestException(ExceptionMessage.MEMBER_UPDATE_FAILED);
        }
    }

    @Override
    public void deleteMember(Long id) {
        // 사용자 존재 확인
        memberRepository.findMemberById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND));

        int result = memberRepository.deleteMember(id);
        if (result == 0) {
            throw new BadRequestException(ExceptionMessage.MEMBER_DELETE_FAILED);
        }
    }
}
