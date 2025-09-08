package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dao.MeetingRepository;
import com.sys.stm.domains.meeting.domain.Meeting;
import com.sys.stm.domains.meeting.domain.MeetingParticipant;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequest;
import com.sys.stm.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingParticipantService meetingParticipantService;
    private final NaverApiService naverApiService;

    @Override
    public void createMeeting(MeetingCreateRequest request, MultipartFile audioFile, Long memberId) {

        // TODO :: Member, Project 연동시 연결
        Long projectId = 1L;

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

    @Override
    public Meeting getMeetingWithParticipants(Long meetingId) {
        return null;
    }

    @Override
    public Long createMeetingWithParticipants(MeetingCreateRequest request, Long projectId, Long memberId, List<Long> participantMemberIds) {
        return 0L;
    }
}
