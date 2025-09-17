package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dto.request.MeetingSendEmailRequestDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/** 작성자: 배지원 */
// EmailServiceImpl.java
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(MeetingSendEmailRequestDTO emailRequest, List<MultipartFile> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 발신자 설정
            helper.setFrom(fromEmail);

            // 수신자 설정 (1명 또는 여러명 모두 처리)
            if (emailRequest.getTo() != null && !emailRequest.getTo().isEmpty()) {
                // List<String>을 String[]로 변환하여 전달
                helper.setTo(emailRequest.getTo().toArray(new String[0]));
            }

//            // 참조 설정
//            if (emailRequest.getCc() != null && !emailRequest.getCc().isEmpty()) {
//                helper.setCc(emailRequest.getCc().toArray(new String[0]));
//            }
//
//            // 숨은 참조 설정
//            if (emailRequest.getBcc() != null && !emailRequest.getBcc().isEmpty()) {
//                helper.setBcc(emailRequest.getBcc().toArray(new String[0]));
//            }

            // 제목 설정
            helper.setSubject(emailRequest.getSubject());

            // 내용 설정 (HTML 형식)
            helper.setText(emailRequest.getContent(), true);

            // 첨부파일 처리
            if (attachments != null && !attachments.isEmpty()) {
                for (MultipartFile attachment : attachments) {
                    if (!attachment.isEmpty()) {
                        helper.addAttachment(
                                attachment.getOriginalFilename(),
                                new ByteArrayResource(attachment.getBytes())
                        );
                    }
                }
            }

            // 이메일 전송
            mailSender.send(message);

            log.info("이메일 전송 성공: {}", emailRequest.getTo());

        } catch (Exception e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
        }
    }
}