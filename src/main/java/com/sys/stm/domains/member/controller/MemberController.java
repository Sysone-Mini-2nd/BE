package com.sys.stm.domains.member.controller;

import com.sys.stm.domains.member.dto.request.MemberCreateRequestDTO;
import com.sys.stm.domains.member.dto.request.MemberUpdateRequestDTO;
import com.sys.stm.domains.member.dto.response.MemberResponseDTO;
import com.sys.stm.domains.member.service.MemberService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createMember(@Valid @RequestBody MemberCreateRequestDTO memberCreateRequestDTO) {
        memberService.createMember(memberCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("사용자가 성공적으로 등록되었습니다."));
    }

    /**
     * 사용자 조회 (ID로)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMemberById(@PathVariable Long id) {
        MemberResponseDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.ok(member, "사용자 조회 성공"));
    }

    /**
     * 모든 사용자 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponseDTO>>> getAllMembers() {
        List<MemberResponseDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.ok(members, "사용자 목록 조회 성공"));
    }

    /**
     * 사용자 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO) {
        memberService.updateMember(id, memberUpdateRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok("사용자 정보가 성공적으로 수정되었습니다."));
    }

    /**
     * 사용자 삭제 (논리 삭제)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.ok("사용자가 성공적으로 삭제되었습니다."));
    }
}
