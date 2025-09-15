package com.sys.stm.domains.auth.service;

import com.sys.stm.domains.auth.dto.request.LoginRequestDTO;
import com.sys.stm.domains.auth.dto.request.RefreshTokenRequestDTO;
import com.sys.stm.domains.auth.dto.response.LoginResponseDTO;
import com.sys.stm.domains.member.dao.MemberRepository;
import com.sys.stm.domains.member.domain.Member;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    /**
     * 로그인 처리
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getAccountId(),
                    loginRequest.getPassword()
                )
            );

            // 사용자 정보 조회
            Member member = memberRepository.findByAccountId(loginRequest.getAccountId())
                    .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND));

            // 마지막 로그인 시간 업데이트
            updateLastLoginTime(member.getId());

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(
                member.getId(),
                member.getAccountId(),
                member.getRole()
            );
            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

            log.info("User '{}' logged in successfully", member.getAccountId());

            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration / 1000) // 초 단위로 변환
                    .userId(member.getId())
                    .accountId(member.getAccountId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .picUrl(member.getPicUrl())
                    .position(member.getPosition())
                    .build();

        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getAccountId(), e);
            throw new BadRequestException(ExceptionMessage.LOGIN_FAILED);
        }
    }

    /**
     * 토큰 갱신
     */
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();

        // Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new BadRequestException(ExceptionMessage.INVALID_TOKEN);
        }

        try {
            // Refresh Token에서 사용자 ID 추출
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

            // 사용자 정보 조회
            Member member = memberRepository.findMemberById(userId)
                    .map(dto -> Member.builder()
                            .id(dto.getId())
                            .accountId(dto.getAccountId())
                            .role(dto.getRole())
                            .email(dto.getEmail())
                            .name(dto.getName())
                            .picUrl(dto.getPicUrl())
                            .position(dto.getPosition())
                            .build())
                    .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND));

            // 새로운 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(
                member.getId(),
                member.getAccountId(),
                member.getRole()
            );
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

            log.info("Token refreshed for user: {}", member.getAccountId());

            return LoginResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtExpiration / 1000)
                    .userId(member.getId())
                    .accountId(member.getAccountId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .picUrl(member.getPicUrl())
                    .position(member.getPosition())
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new BadRequestException(ExceptionMessage.TOKEN_REFRESH_FAILED);
        }
    }

    /**
     * 마지막 로그인 시간 업데이트
     */
    private void updateLastLoginTime(Long memberId) {
        try {
            memberRepository.updateLastLoginTime(memberId, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            log.warn("Failed to update last login time for user ID: {}", memberId, e);
        }
    }
}
