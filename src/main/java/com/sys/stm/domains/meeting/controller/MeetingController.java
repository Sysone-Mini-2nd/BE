package com.sys.stm.domains.meeting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.request.MeetingSendEmailRequestDTO;
import com.sys.stm.domains.meeting.dto.request.MeetingUpdateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.*;
import com.sys.stm.domains.meeting.service.EmailService;
import com.sys.stm.domains.meeting.service.MeetingService;
import com.sys.stm.domains.meeting.service.NaverApiService;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final NaverApiService naverApiService;
    private final MeetingService meetingService;
    private final EmailService emailService;

    @PostMapping(value = "/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> receiveAndSummarizeAudio(
            @RequestParam("audio") MultipartFile audioFile) {

        // 파일 처리 로직은 Service 계층에 위임
        String summary = naverApiService.processAudio(audioFile);

        // 처리 결과를 클라이언트에 반환
        return ApiResponse.ok(200, summary, "음성 파일 TEXT 변환 성공");
    }

    // 회의록 생성
    @PostMapping(value = "/{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> createMeeting(
            @PathVariable Long projectId,
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

        meetingService.createMeeting(request,audioFile,memberId,projectId);

        return ApiResponse.ok(200, null, "회의록 생성 성공");
    }


    // 회의록 상세 조회
    @GetMapping("/{projectId}/{meetingId}")
    public ApiResponse<MeetingDetailResponseDTO> getMeeting(@PathVariable(name = "projectId") Long projectId,  @PathVariable(name = "meetingId") Long meetingId) {


        MeetingDetailResponseDTO response = meetingService.getMeetingWithParticipants(projectId,meetingId);

        return ApiResponse.ok(200, response, "회의록 세부 조회 성공");
    }

    // 프로젝트별 회의 목록 조회 (Oracle ROWNUM 페이지네이션, 간소화된 MeetingListResponseDTO 반환)
    @GetMapping("/{projectId}")
    public ApiResponse<MeetingListPageResponseDTO<MeetingListResponseDTO>> getMeetingsByProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String progressDate,
            @RequestParam(required = false) String keyword) {

        MeetingListPageResponseDTO<MeetingListResponseDTO> response = meetingService.getMeetingList(projectId, page, size, progressDate, keyword);
        return ApiResponse.ok(200, response, "프로젝트별 회의 목록 조회 성공");
    }


    @DeleteMapping("/{projectId}/{meetingId}")
    public ApiResponse<String> deleteMeeting(
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "meetingId") Long meetingId
    ){
        meetingService.deleteMeeting(meetingId);

        return ApiResponse.ok(200, null, "회의록 삭제가 완료되었습니다.");
    }

    @PutMapping("/{projectId}/{meetingId}")
    public ApiResponse<String> updateMeeting(
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "meetingId") Long meetingId,
            @RequestBody MeetingUpdateRequestDTO requestDTO
    ){
        meetingService.updateMeeting(requestDTO,meetingId);

        return ApiResponse.ok(200, null, "회의록 수정이 완료되었습니다.");
    }


    // AI 내용 정리
    @GetMapping("/{projectId}/{meetingId}/report")
    public ApiResponse<MeetingAnalysisResponseDTO> getMeetingReport(
            @PathVariable(name = "meetingId") Long meetingId
    ){
        Long memberId = 1L;

        MeetingAnalysisResponseDTO response = meetingService.getMeetingAiData(meetingId);

        return ApiResponse.ok(200, response, "회의록 AI를 통한 정리 성공");
    }

    // AI 문서 자동화 작성 및 파일 다운로드
    @GetMapping("/{projectId}/{meetingId}/report/download")
    public ResponseEntity<byte[]> downloadMeetingReport(
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "meetingId") Long meetingId
    ) {
        Long memberId = 1L;

        try {
            // Service에서 Map으로 받아옴
            Map<String, Object> reportData = meetingService.downloadMeetingReport(projectId, meetingId);

            MeetingDetailResponseDTO meetingDetail = (MeetingDetailResponseDTO) reportData.get("meetingDetail");
            byte[] reportBytes = (byte[]) reportData.get("reportBytes");
            String fileName = (String) reportData.get("fileName");

            // 한글 파일명 처리
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentLength(reportBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 이메일 전송
    @PostMapping("/{projectId}/{meetingId}/email")
    public ApiResponse<String> sendEmail(
            @PathVariable(name = "projectId") Long projectId,
            @PathVariable(name = "meetingId") Long meetingID,
            @RequestParam("emailData") String emailDataJson,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments
    ){

        try {
            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            MeetingSendEmailRequestDTO emailRequest = mapper.readValue(emailDataJson, MeetingSendEmailRequestDTO.class);

            // 이메일 전송
            emailService.sendEmail(emailRequest, attachments);

            return ApiResponse.ok(200, null, "이메일 전송 성공");

        } catch (Exception e) {
            throw new BadRequestException(ExceptionMessage.EMAIL_BAD_REQUEST);
        }
    }
}
