package com.sys.stm.domains.dashBoard.controller;

import com.sys.stm.domains.dashBoard.dto.response.DashBoardProjectListResponseDTO;
import com.sys.stm.domains.dashBoard.dto.response.DashBoardResponseDTO;
import com.sys.stm.domains.dashBoard.service.DashBoardServiceImpl;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardServiceImpl dashBoardService;


    @GetMapping("")
    public ApiResponse<DashBoardProjectListResponseDTO> getProjectsByMemberId(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();
        String memberRole = userDetails.getRole();

        DashBoardProjectListResponseDTO response = dashBoardService.getProjectsByMemberId(memberId, memberRole);

        return ApiResponse.ok(200, response, "프로젝트 리스트 호출 성공");
    }


    @GetMapping("/{projectId}")
    public ApiResponse<DashBoardResponseDTO> getDashBoard(
            @PathVariable(name = "projectId") Long projectId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long memberId = userDetails.getId();
        String loginMemberRole = userDetails.getRole();

        DashBoardResponseDTO response = dashBoardService.findDashBoard(memberId, projectId);


        return ApiResponse.ok(200,response,"대시보드 데이터 호출 성공");
    }
}
