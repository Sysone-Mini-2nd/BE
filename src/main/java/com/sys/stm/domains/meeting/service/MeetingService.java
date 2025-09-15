package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDashBoardResponseDTO;
import com.sys.stm.domains.meeting.dto.request.MeetingCreateRequestDTO;
import com.sys.stm.domains.meeting.dto.request.MeetingUpdateRequestDTO;
import com.sys.stm.domains.meeting.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MeetingService {

    void createMeeting(MeetingCreateRequestDTO request, MultipartFile audioFile, Long memberId, Long projectId);

    // Meeting과 Participant 함께 조회
    MeetingDetailResponseDTO getMeetingWithParticipants(Long projectId, Long meetingId);

    // 페이지네이션을 위한 회의 목록 조회 (검색 기능 포함)
    MeetingListPageResponseDTO<MeetingListResponseDTO> getMeetingList(Long projectId, int page, int size, String progressDate, String keyword);

    void deleteMeeting(Long meetingId, Long memberId);

    MeetingAnalysisResponseDTO getMeetingAiData(Long meetingId);

    Map<String, Object> downloadMeetingReport(Long projectId, Long meetingId);

    void updateMeeting(MeetingUpdateRequestDTO meetingUpdateRequestDTO, Long meetingId, Long memberId);

    List<AssignedPersonDashBoardResponseDTO> getProjectParticipant(Long projectId);
}
