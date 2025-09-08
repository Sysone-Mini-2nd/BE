package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dao.MeetingParticipantRepository;
import com.sys.stm.domains.meeting.domain.MeetingParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public List<MeetingParticipant> findByMeetingId(Long meetingId) {
        return List.of();
    }

    @Override
    public Optional<MeetingParticipant> findById(Long id) {
        return Optional.empty();
    }
}
