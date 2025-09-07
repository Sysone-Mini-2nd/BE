package com.sys.stm.domains.messenger.controller;

import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import com.sys.stm.domains.messenger.service.MemberProfileService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/messenger")
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    // 본인 프로필 정보 조회
    @GetMapping("/profile")
    public ApiResponse<MemberProfileResponseDto> findMemberProfileById() {
        //TODO SecurityContextHolder에 있는 Member 객체 가져오기, 일단 지금은 member id 하드코딩
        long id = 1;
        MemberProfileResponseDto memberProfileResponseDto = memberProfileService.findMemberProfileById(id);
        return ApiResponse.ok(200, memberProfileResponseDto, "사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

    // 모든 사원 프로필 정보 조회
    @GetMapping("/all-profiles")
    public ApiResponse<List<MemberProfileResponseDto>> findAllMemberProfiles() {
        List<MemberProfileResponseDto> memberProfileResponseDtoList = memberProfileService.findAllMemberProfiles();
        return ApiResponse.ok(200, memberProfileResponseDtoList, "전체 사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

    // 이메일, 이름으로 특정 사원 프로필 정보 조회
    @GetMapping("/profiles")
    public ApiResponse<List<MemberProfileResponseDto>> findMemberProfilesByEmailOrName(@RequestParam(name = "keyword") String keyword) {
        List<MemberProfileResponseDto> memberProfileResponseDtoList = memberProfileService.findMemberProfilesByEmailOrName(keyword);
        return ApiResponse.ok(200, memberProfileResponseDtoList, "전체 사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

}
