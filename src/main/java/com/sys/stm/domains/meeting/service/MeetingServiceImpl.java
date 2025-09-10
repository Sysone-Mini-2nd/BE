package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dao.MeetingRepository;
import com.sys.stm.domains.meeting.domain.Meeting;
import com.sys.stm.domains.meeting.domain.MeetingParticipant;
import com.sys.stm.domains.meeting.domain.Participant;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.*;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantService meetingParticipantService;
    private final NaverApiService naverApiService;
    private final MeetingAnalysisService meetingAnalysisService;
    private final MeetingReportService meetingReportService;

    @Override
    public void createMeeting(MeetingCreateRequestDTO request, MultipartFile audioFile, Long memberId, Long projectId) {

        // TODO :: Member, Project 연동시 연결
        // 음성파일이 있으면 음성인식 후 content에 추가
        String finalContent = request.getContent();
        if (audioFile != null && !audioFile.isEmpty()) {
            // 오디오파일 Text 변환
            String audioSummary = naverApiService.processAudio(audioFile);

            finalContent = (request.getContent() != null ? request.getContent() + "\n\n" : "") + "[음성인식 결과]\n" + audioSummary;
        }


        // 회의록 저장
        Meeting meeting = new Meeting(request, projectId, memberId, finalContent);
        int meetingResult = meetingRepository.createMeeting(meeting);

        if (meetingResult <= 0) {
            throw new RuntimeException(ExceptionMessage.MEETING_RUNTIME_ERROR.getMessage());
        }

        // 생성된 회의록 ID 기준으로 참여자 Entity 리스트 생성
        for (Long participantMemberId : request.getParticipantIds()) {
            MeetingParticipant participant = MeetingParticipant.builder()
                    .meetingId(meeting.getId())  // 생성된 Meeting ID 사용
                    .memberId(participantMemberId)
                    .build();

            // 참여자 테이블 저장
            meetingParticipantService.createParticipant(participant);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public MeetingDetailResponseDTO getMeetingWithParticipants(Long projectId, Long meetingId) {
        // ResultMap을 사용한 단일 쿼리로 조회

        Meeting meeting = meetingRepository.findByIdWithParticipants(meetingId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.MEETING_NOT_FOUND));

        // TODO :: 이름 부분 유저 연동되면 수정
        List<MeetingParticipantResponseDTO> participantResponse = meeting.getParticipants().stream()
                .map(participant -> MeetingParticipantResponseDTO.builder()
                        .id(participant.getId())
                        .name("test")
                        .build())
                .toList();


        MeetingDetailResponseDTO response = MeetingDetailResponseDTO.builder()
                .title(meeting.getTitle())
                .progressDate(meeting.getProgressDate())
                .participants(participantResponse)
                .writerName("작성자 Test")
                .place(meeting.getPlace())
                .content(meeting.getContent())
                .build();

        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public MeetingListPageResponseDTO<MeetingListResponseDTO> getMeetingList(Long projectId, int page, int size, String progressDate, String keyword) {


        // 1. 검색 조건을 포함한 전체 개수 조회
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("projectId", projectId);
        searchParams.put("progressDate", progressDate);
        searchParams.put("keyword", keyword);

        // 1. 전체 개수 조회
        int totalCount = meetingRepository.countByProjectIdWithSearch(searchParams);

        // 2. Oracle 페이지네이션 파라미터 계산
        int startRow = (page - 1) * size + 1;
        int endRow = page * size;

        // 3. 파라미터 맵 생성 (검색 조건 포함)
        Map<String, Object> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("progressDate", progressDate);
        params.put("keyword", keyword);
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        // 4. 회의 목록 조회
        List<Meeting> meetings = meetingRepository.findByProjectIdWithPagination(params);

        // 5. 각 회의의 참석자 데이터 추가 (배치 조회로 성능 최적화)
        if (!meetings.isEmpty()) {
            // 회의 ID 목록 추출
            List<Long> meetingIds = meetings.stream()
                    .map(Meeting::getId)
                    .collect(Collectors.toList());

            Map<Long, List<MeetingParticipant>> participantsByMeetingId = meetingParticipantService.findByMeetingIds(meetingIds);

            // 각 회의에 참석자 데이터 설정
            for (Meeting meeting : meetings) {
                List<MeetingParticipant> participants =
                        participantsByMeetingId.getOrDefault(meeting.getId(), new ArrayList<>());
                meeting.setParticipants(participants);
            }
        }

        // TODO 유저 연동되면 수정
        List<MeetingListResponseDTO> response = meetings.stream()
                .map(meeting -> MeetingListResponseDTO.builder()
                        .title(meeting.getTitle())
                        .writerName("test")
                        .type(meeting.getType())
                        .progressTime(meeting.getProgressDate())
                        .place(meeting.getPlace())
                        .attendeeCount(meeting.getParticipants() != null ? meeting.getParticipants().size() : 0)
                        .build())
                .toList();

        // 7. 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean hasNext = page < totalPages;
        boolean hasPrevious = page > 1;

        return MeetingListPageResponseDTO.<MeetingListResponseDTO>builder()
                .content(response)
                .page(page)
                .size(size)
                .totalElements(totalCount)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

    }

    @Override
    public void deleteMeeting(Long meetingId) {

        Meeting meeting = validateMeetingExists(meetingId);

        // 회의록 삭제
        meetingRepository.deleteMeeting(meeting);

        // 회의록에 연관된 데이터 삭제
        // 1. 참석자 데이터 삭제
        meeting.getParticipants().stream()
                .forEach(
                        meetingParticipant -> {
                            meetingParticipantService.deleteParticipant(meetingParticipant.getId());
                        }
                );


    }



    @Override
    @Transactional(readOnly = true)
    public MeetingAnalysisResponseDTO getMeetingAiData(Long meetingId) {
        Meeting meeting = validateMeetingExists(meetingId);

        MeetingAnalysisResponseDTO response = meetingAnalysisService.analyzeMeetingContent(meeting.getContent());

        return response;
    }

    @Override
    public Map<String, Object>  downloadMeetingReport(Long projectId, Long meetingId) {
        Meeting meeting = validateMeetingExists(meetingId);

        MeetingAnalysisResponseDTO meetingAnalysisResponseDTO = meetingAnalysisService.analyzeMeetingContent(meeting.getContent());

        // 회의록 상세 정보 조회
        MeetingDetailResponseDTO meetingDetail = getMeetingWithParticipants(projectId, meetingId);

        // 템플릿 기반 보고서 생성
        byte[] reportBytes = meetingReportService.fillMeetingTemplate(meetingDetail,meetingAnalysisResponseDTO);

        // 파일명 생성
        String fileName = "회의록_" + meetingDetail.getTitle() + "_" +
                meetingDetail.getProgressDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".docx";

        Map<String, Object> result = new HashMap<>();
        result.put("meetingDetail", meetingDetail);
        result.put("reportBytes", reportBytes);
        result.put("fileName", fileName);

        return result;
    }


    @Override
    public Long createMeetingWithParticipants(MeetingCreateRequestDTO request, Long projectId, Long memberId, List<Long> participantMemberIds) {
        return 0L;
    }




    protected Meeting validateMeetingExists(Long meetingId) {
        return  meetingRepository.getMeetingById(meetingId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.MEETING_NOT_FOUND));
    }

}
