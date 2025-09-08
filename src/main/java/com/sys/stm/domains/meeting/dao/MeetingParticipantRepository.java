package com.sys.stm.domains.meeting.dao;

import com.sys.stm.domains.meeting.domain.MeetingParticipant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface MeetingParticipantRepository {

    // 참석자 생성
    void createParticipant(MeetingParticipant participant);

    // 회의별 참석자 목록 조회
    List<MeetingParticipant> findByMeetingId(Long meetingId);

    // 참석자 조회 (ID로)
    Optional<MeetingParticipant> findById(Long id);

    // 회의 ID 목록으로 참석자 배치 조회
    List<MeetingParticipant> findByMeetingIds(List<Long> meetingIds);
}
