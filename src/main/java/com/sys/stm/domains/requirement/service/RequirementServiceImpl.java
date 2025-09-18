package com.sys.stm.domains.requirement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.domains.issue.dto.response.IssueMockResponseDTO;
import com.sys.stm.domains.requirement.adapter.AiClient;
import com.sys.stm.domains.requirement.dto.response.AiAnalysisResponseDTO;
import com.sys.stm.domains.requirement.util.ExcelParser;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/** 작성자: 백승준 */
@Service
@RequiredArgsConstructor
public class RequirementServiceImpl implements RequirementService {

    private final ExcelParser excelParser;
    private final AiClient aiClient;
    private final ObjectMapper objectMapper;

    @Override
    public List<IssueMockResponseDTO> analyzeRequirements(MultipartFile file) {
        try {
            List<Map<String, Object>> parsedData = excelParser.parse(file);
            String jsonData = objectMapper.writeValueAsString(parsedData);
            String aiResponseJson = aiClient.analyzeRequirements(jsonData);

            String cleanedJson = cleanAiResponse(aiResponseJson);

            AiAnalysisResponseDTO analysisResult = objectMapper.readValue(cleanedJson, AiAnalysisResponseDTO.class);

            if (analysisResult != null && analysisResult.getIssues() != null) {
                return analysisResult.getIssues();
            }

            return Collections.emptyList();

        } catch (IOException e) {
            throw new InvalidFileException(ExceptionMessage.FILE_PROCESSING_ERROR);
        }
    }

    private String cleanAiResponse(String rawResponse) {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            return "{}";
        }

        int firstBrace = rawResponse.indexOf('{');
        int lastBrace = rawResponse.lastIndexOf('}');

        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return rawResponse.substring(firstBrace, lastBrace + 1);
        }

        return "{}";
    }
}
