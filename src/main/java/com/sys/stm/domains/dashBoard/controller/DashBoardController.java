package com.sys.stm.domains.dashBoard.controller;

import com.sys.stm.domains.dashBoard.dto.response.DashBoardUserResponseDTO;
import com.sys.stm.domains.dashBoard.service.DashBoardServiceImpl;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dashboards")
@RequiredArgsConstructor
public class
DashBoardController {

    private final DashBoardServiceImpl boardServiceImpl;


    @GetMapping("/")
    public ApiResponse<DashBoardUserResponseDTO> getUserDashBoard(){
        // TODO 유저 연동시 변동
        Long memberId = 1L;


        return ApiResponse.ok(200,null,"대시보드 데이터 호출 성공");
    }
}
