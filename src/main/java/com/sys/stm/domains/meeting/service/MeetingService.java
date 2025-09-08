package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingDetailResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeetingService {

    void createMeeting(MeetingCreateRequestDTO request, MultipartFile audioFile, Long memberId);

    // Meeting과 Participant 함께 조회
    MeetingDetailResponseDTO getMeetingWithParticipants(Long meetingId);

    // Meeting과 Participant 함께 생성
    Long createMeetingWithParticipants(MeetingCreateRequestDTO request, Long projectId, Long memberId, List<Long> participantMemberIds);
}
