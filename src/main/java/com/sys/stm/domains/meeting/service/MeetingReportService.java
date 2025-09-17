package com.sys.stm.domains.meeting.service;

import com.sys.stm.domains.meeting.dto.response.MeetingDetailResponseDTO;
import com.sys.stm.domains.meeting.dto.response.MeetingAnalysisResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/** 작성자: 배지원 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingReportService {

    @Value("${app.report.save-path:./reports}")
    private String reportSavePath;

    public byte[] fillMeetingTemplate(MeetingDetailResponseDTO meetingDetail, MeetingAnalysisResponseDTO meetingAnalysis) {
        try {
            // .docx 템플릿 파일 로드
            ClassPathResource templateResource = new ClassPathResource("templates/meeting_template.docx");
            InputStream templateStream = templateResource.getInputStream();

            // XWPFDocument 사용 (.docx 파일용)
            XWPFDocument document = new XWPFDocument(templateStream);

            // 텍스트 치환
            replaceTextInDocument(document, meetingDetail, meetingAnalysis);

            // 문서를 바이트 배열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            document.close();
            templateStream.close();

            byte[] reportBytes = outputStream.toByteArray();

            // 서버에 파일 저장
            saveReportToFile(reportBytes, meetingDetail.getTitle());

            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("템플릿 처리 중 오류 발생", e);
            throw new RuntimeException("템플릿 처리에 실패했습니다.", e);
        }
    }

    private void replaceTextInDocument(XWPFDocument document, MeetingDetailResponseDTO meetingDetail, MeetingAnalysisResponseDTO analysis) {
        // 모든 단락을 순회하면서 텍스트 치환
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String originalText = paragraph.getText();
            if (originalText == null || originalText.isEmpty()) {
                continue;
            }

            String replacedText = replacePlaceholders(originalText, meetingDetail, analysis);

            // 텍스트가 변경되었으면
            if (!replacedText.equals(originalText)) {
                // 기존 Run들을 모두 제거하고 새로 생성
                removeAllRuns(paragraph);

                // 새로운 Run 생성
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(replacedText);
            }
        }

        // 모든 테이블에서도 동일하게 처리
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph cellParagraph : cell.getParagraphs()) {
                        String originalText = cellParagraph.getText();
                        if (originalText == null || originalText.isEmpty()) {
                            continue;
                        }

                        String replacedText = replacePlaceholders(originalText, meetingDetail, analysis);

                        if (!replacedText.equals(originalText)) {
                            removeAllRuns(cellParagraph);
                            XWPFRun newRun = cellParagraph.createRun();
                            newRun.setText(replacedText);
                        }
                    }
                }
            }
        }
    }

    private void removeAllRuns(XWPFParagraph paragraph) {
        // Run을 하나씩 제거
        while (paragraph.getRuns().size() > 0) {
            paragraph.removeRun(0);
        }
    }

    private String replacePlaceholders(String text, MeetingDetailResponseDTO meetingDetail, MeetingAnalysisResponseDTO analysis) {
        String replacedText = text;

        // 회의 정보 치환
        replacedText = replacedText.replace("${meetingTitle}", meetingDetail.getTitle() != null ? meetingDetail.getTitle() : "");
        replacedText = replacedText.replace("${meetingDate}",
                meetingDetail.getProgressDate() != null ?
                        meetingDetail.getProgressDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) : "");
        replacedText = replacedText.replace("${meetingTime}",
                meetingDetail.getProgressDate() != null ?
                        meetingDetail.getProgressDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        replacedText = replacedText.replace("${meetingPlace}", meetingDetail.getPlace() != null ? meetingDetail.getPlace() : "");
        replacedText = replacedText.replace("${writerName}", meetingDetail.getWriterName() != null ? meetingDetail.getWriterName() : "");

        // 회의 내용 치환
        replacedText = replacedText.replace("${meetingContent}", meetingDetail.getContent() != null ? meetingDetail.getContent() : "");

        // AI 분석 결과 치환
        if (analysis != null) {
            // 주요 주제 - 번호 매기기
            String mainTopics = formatListWithNumbers(analysis.getMainTopics());
            replacedText = replacedText.replace("${mainTopics}", mainTopics);

            // 의사 결정 사항 - 번호 매기기
            String decisions = formatListWithNumbers(analysis.getDecisions());
            replacedText = replacedText.replace("${decisions}", decisions);

            // 우선순위 할 일 - 번호 매기기
            String priorities = formatListWithNumbers(analysis.getPriorities());
            replacedText = replacedText.replace("${priorities}", priorities);
        }

        // 참석자 목록 치환
        if (meetingDetail.getParticipants() != null && !meetingDetail.getParticipants().isEmpty()) {
            String participants = meetingDetail.getParticipants().stream()
                    .map(p -> p.getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            replacedText = replacedText.replace("${participants}", participants);
        }

        return replacedText;
    }


    private void saveReportToFile(byte[] reportBytes, String title) {
        try {
            // 저장 디렉토리 생성
            Path reportDir = Paths.get(reportSavePath);
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }

            // 파일명 생성 - %s로 수정 (String 타입)
            String fileName = String.format("meeting_report_%s_%s.docx",
                    title,  // String 타입이므로 %s 사용
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));

            // 파일 저장
            Path filePath = reportDir.resolve(fileName);
            Files.write(filePath, reportBytes);

            log.info("보고서가 저장되었습니다: {}", filePath.toString());

        } catch (IOException e) {
            log.error("보고서 저장 중 오류 발생", e);
            // 저장 실패해도 다운로드는 계속 진행
        }
    }

    // 번호 매기기 및 줄바꿈 포맷팅 메서드 추가
    private String formatListWithNumbers(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            formatted.append(i + 1).append(". ").append(items.get(i));
            if (i < items.size() - 1) {
                formatted.append("\n\n"); // 항목 사이에 한 줄 띄기
            }
            formatted.append("\n\n"); // 항목 사이에 한 줄 띄기
        }

        return formatted.toString();
    }
}