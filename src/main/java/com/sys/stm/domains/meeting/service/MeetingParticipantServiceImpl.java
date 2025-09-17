package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dao.MeetingParticipantRepository;
import com.sys.stm.domains.meeting.domain.MeetingParticipant;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/** 작성자: 배지원 */
@Service
@RequiredArgsConstructor
@Transactional
public class MeetingParticipantServiceImpl implements MeetingParticipantService {
    private final MeetingParticipantRepository meetingParticipantRepository;

    @Override
    public void createParticipant(MeetingParticipant participant) {
        meetingParticipantRepository.createParticipant(participant);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MeetingParticipant> findByMeetingId(Long meetingId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MeetingParticipant> findById(Long id) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<MeetingParticipant>> findByMeetingIds(List<Long> meetingIds) {
        // 참석자 데이터 배치 조회
        Map<Long, List<MeetingParticipant>> participantsByMeetingId =
                meetingParticipantRepository.findByMeetingIds(meetingIds)
                        .stream()
                        .collect(Collectors.groupingBy(MeetingParticipant::getMeetingId));

        return participantsByMeetingId;
    }

    @Override
    public void deleteParticipant(Long meetingParticipantId) {
        MeetingParticipant meetingParticipant = validateMeetingParticipantExists(meetingParticipantId);

        meetingParticipantRepository.deleteMeetingParticipant(meetingParticipant);
    }


    protected MeetingParticipant  validateMeetingParticipantExists(Long meetingParticipantId) {
        return meetingParticipantRepository.getParticipantById(meetingParticipantId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.MEETING_PARTICIPANT_NOT_FOUND));
    }


}
