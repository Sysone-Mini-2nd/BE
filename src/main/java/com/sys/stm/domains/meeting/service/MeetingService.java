package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.domain.Meeting;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeetingService {

    void createMeeting(MeetingCreateRequest request,MultipartFile audioFile,Long memberId);

    // Meeting과 Participant 함께 조회
    Meeting getMeetingWithParticipants(Long meetingId);

    // Meeting과 Participant 함께 생성
    Long createMeetingWithParticipants(MeetingCreateRequest request, Long projectId, Long memberId, List<Long> participantMemberIds);
}
