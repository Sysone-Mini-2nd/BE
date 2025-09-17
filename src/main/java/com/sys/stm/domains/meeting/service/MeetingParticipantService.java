package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.domain.MeetingParticipant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
/** 작성자: 배지원 */
public interface MeetingParticipantService {

    // 참석자 생성
    void createParticipant(MeetingParticipant participant);

    // 회의별 참석자 목록 조회
    List<MeetingParticipant> findByMeetingId(Long meetingId);

    // 참석자 조회 (ID로)
    Optional<MeetingParticipant> findById(Long id);

    Map<Long, List<MeetingParticipant>> findByMeetingIds(List<Long> meetingIds);

    void deleteParticipant(Long meetingParticipantId);
}
