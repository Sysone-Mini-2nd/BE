package com.sys.stm.domains.messenger.controller;

import com.sys.stm.domains.messenger.dto.request.MemberProfileUpdateRequestDto;
import com.sys.stm.domains.messenger.dto.response.MemberProfileResponseDto;
import com.sys.stm.domains.messenger.service.MemberProfileService;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/** 작성자: 조윤상 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    // 본인 프로필 정보 조회
    @GetMapping("")
    public ApiResponse<MemberProfileResponseDto> findMemberProfileById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberProfileResponseDto memberProfileResponseDto = memberProfileService.findMemberProfileById(userDetails.getId());
        return ApiResponse.ok(200, memberProfileResponseDto, "사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

    // 모든 사원 프로필 정보 조회
    @GetMapping("/all")
    public ApiResponse<List<MemberProfileResponseDto>> findAllMemberProfiles(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long memberId = userDetails.getId();
        List<MemberProfileResponseDto> memberProfileResponseDtoList = memberProfileService
                .findAllMemberProfiles()
                .stream()
                .filter(profile -> profile.getId() != memberId) // 로그인한 사용자 제외
                .toList();
        return ApiResponse.ok(200, memberProfileResponseDtoList, "전체 사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

    // 이메일, 이름으로 특정 사원 프로필 정보 조회
    @GetMapping("/search")
    public ApiResponse<List<MemberProfileResponseDto>> findMemberProfilesByEmailOrName(@RequestParam(name = "keyword") String keyword) {
        List<MemberProfileResponseDto> memberProfileResponseDtoList = memberProfileService.findMemberProfilesByEmailOrName(keyword);
        return ApiResponse.ok(200, memberProfileResponseDtoList, "전체 사원 프로필 조회 요청이 성공적으로 처리되었습니다.");
    }

    // 본인 프로필 정보 수정
    @PutMapping("/update")
    public ApiResponse<?> updateMemberProfile(@RequestBody MemberProfileUpdateRequestDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        int updateMemberCount = memberProfileService.updateMemberProfile(userDetails.getId(), dto);

        if (updateMemberCount == 1)
            return ApiResponse.ok();
        else
            throw new RuntimeException("프로필 업데이트에 실패했습니다. 다시 시도해주세요.");
    }
}
