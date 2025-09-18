package com.sys.stm.domains.auth.controller;

import com.sys.stm.domains.auth.dto.request.LoginRequestDTO;
import com.sys.stm.domains.auth.dto.request.RefreshTokenRequestDTO;
import com.sys.stm.domains.auth.dto.response.LoginResponseDTO;
import com.sys.stm.domains.auth.service.AuthService;
import com.sys.stm.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/** 작성자: 김대호 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.ok(response, "로그인 성공"));
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshRequest) {
        LoginResponseDTO response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(ApiResponse.ok(response, "토큰 갱신 성공"));
    }

    /**
     * 로그아웃 (클라이언트에서 토큰 삭제)
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // JWT는 stateless하므로 서버에서는 특별한 처리가 필요 없음
        // 클라이언트에서 토큰을 삭제하면 됨
        return ResponseEntity.ok(ApiResponse.ok("로그아웃 성공"));
    }
}
