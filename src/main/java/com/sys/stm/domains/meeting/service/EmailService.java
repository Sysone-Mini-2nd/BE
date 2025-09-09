package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dto.request.MeetingSendEmailRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface EmailService {
    void sendEmail(MeetingSendEmailRequestDTO emailRequest, List<MultipartFile> attachments);
}
