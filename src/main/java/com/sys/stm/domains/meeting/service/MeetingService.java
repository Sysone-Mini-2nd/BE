package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingDetailResponseDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingListPageResponseDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingListResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MeetingService {

    void createMeeting(MeetingCreateRequestDTO request, MultipartFile audioFile, Long memberId, Long projectId);

    // Meeting과 Participant 함께 조회
    MeetingDetailResponseDTO getMeetingWithParticipants(Long projectId, Long meetingId);

    // Meeting과 Participant 함께 생성
    Long createMeetingWithParticipants(MeetingCreateRequestDTO request, Long projectId, Long memberId, List<Long> participantMemberIds);

    // 페이지네이션을 위한 회의 목록 조회 (검색 기능 포함)
    MeetingListPageResponseDTO<MeetingListResponseDTO> getMeetingList(Long projectId, int page, int size, String progressDate, String keyword);

    void deleteMeeting(Long meetingId);
}
