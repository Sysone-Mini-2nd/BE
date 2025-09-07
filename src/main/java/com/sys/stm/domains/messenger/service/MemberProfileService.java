package com.sys.stm.domains.messenger.service;

import com.sys.stm.domains.messenger.dao.MemberProfileMapper;
import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberProfileService {

    private final MemberProfileMapper memberProfileMapper;

    public MemberProfileResponseDto findMemberProfileById(long id) {
        return memberProfileMapper.findMemberProfileById(id);
    }

}
