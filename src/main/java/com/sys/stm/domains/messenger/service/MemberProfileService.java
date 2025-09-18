package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.MemberProfileRepository;
import com.sys.stm.domains.messenger.dto.request.MemberProfileUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/** 작성자: 조윤상 */
@RequiredArgsConstructor
@Transactional
@Service
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;

    public MemberProfileResponseDto findMemberProfileById(long id) {
        return memberProfileRepository.findMemberProfileById(id);
    }

    public List<MemberProfileResponseDto> findAllMemberProfiles() {
        return memberProfileRepository.findAllMemberProfiles();
    }

    public List<MemberProfileResponseDto> findMemberProfilesByEmailOrName(String keyword) {
        // 와일드카드 삽입
        keyword = '%' + keyword + '%';
        return memberProfileRepository.findMemberProfilesByEmailOrName(keyword);
    }

    public int updateMemberProfile(long id, MemberProfileUpdateRequestDto dto) {
        return memberProfileRepository.updateMemberProfile(id, dto);
    }
}
