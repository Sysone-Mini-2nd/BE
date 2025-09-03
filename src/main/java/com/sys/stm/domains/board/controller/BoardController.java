package com.sys.stm.domains.board.controller;

import com.sys.stm.domains.board.service.BoardService;
import com.sys.stm.domains.board.service.BoardServiceImpl;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;

    @GetMapping("/test")
    public ApiResponse<Integer> test() {

        Integer count = boardServiceImpl.testCount();

        return ApiResponse.ok(200, count, "test 입니다.");
    }
}
