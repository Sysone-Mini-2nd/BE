package com.sys.stm.domains.meeting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingDetailResponseDTO;
import com.sys.stm.domains.meeting.service.MeetingService;
import com.sys.stm.domains.meeting.service.NaverApiService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final NaverApiService naverApiService;
    private final MeetingService meetingService;


    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> receiveAndSummarizeAudio(
            @RequestParam("audio") MultipartFile audioFile) {

        // 파일 처리 로직은 Service 계층에 위임
        String summary = naverApiService.processAudio(audioFile);

        // 처리 결과를 클라이언트에 반환
        return ApiResponse.ok(200, summary, "음성 파일 TEXT 변환 성공");
    }

    // 회의록 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> createMeeting(
            @RequestParam("meetingData") String meetingDataJson,
            @RequestParam(value = "audio", required = false) MultipartFile audioFile){
        Long memberId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        MeetingCreateRequestDTO request = null;

        try {
            request = mapper.readValue(meetingDataJson, MeetingCreateRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        meetingService.createMeeting(request,audioFile,memberId);

        return ApiResponse.ok(200, null, "회의록 생성 성공");
    }


    // 회의록 상세 조회
    @GetMapping("/{meetingId}")
    public ApiResponse<MeetingDetailResponseDTO> getMeeting(@PathVariable Long meetingId) {

        MeetingDetailResponseDTO response = meetingService.getMeetingWithParticipants(meetingId);

        return ApiResponse.ok(200, response, "회의록 세부 조회 성공");
    }




}
