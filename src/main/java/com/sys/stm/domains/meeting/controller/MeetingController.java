package com.sys.stm.domains.meeting.controller;

import com.sys.stm.domains.meeting.service.BoardServiceImpl;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;


    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> receiveAndSummarizeAudio(
            @RequestParam("audio") MultipartFile audioFile) {

        // 파일이 비어있는지 확인
//        if (audioFile.isEmpty()) {
//            throw new NotFoundException(ExceptionMessage.VOICE_NOT_FOUND);
//        }

        // 파일 처리 로직은 Service 계층에 위임
        String summary = audioProcessingService.processAudio(audioFile);

        // 처리 결과를 클라이언트에 반환
        return ApiResponse.ok(200, summary, "음성 파일 TEXT 변환 성공");
    }
}
