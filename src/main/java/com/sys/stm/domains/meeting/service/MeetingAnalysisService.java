package com.sys.stm.domains.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.domains.meeting.dto.response.MeetingAnalysisResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingAnalysisService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public MeetingAnalysisResponseDTO analyzeMeetingContent(String meetingContent) {
        try {
            // 프롬프트 템플릿 생성
            String promptTemplate = """
                다음 회의록 내용을 분석하여 다음 3가지로 분류해주세요:
                
                1. 주요주제(주요 안건): 회의에서 논의된 주요 주제들을 나열
                2. 의사 결정 사항: 회의에서 결정된 사항들을 나열
                3. 우선순위 해야 할 일: 앞으로 해야 할 일들을 우선순위 순으로 나열
                
                회의록 내용:
                {content}
                
                응답은 다음 JSON 형식으로만 제공해주세요:
                {
                    "mainTopics": ["주제1", "주제2", "주제3"],
                    "decisions": ["결정사항1", "결정사항2", "결정사항3"],
                    "priorities": ["할일1", "할일2", "할일3"]
                }
                """;

            PromptTemplate template = new PromptTemplate(promptTemplate);
            Prompt prompt = template.create(Map.of("content", meetingContent));

            // OpenAI API 호출
            ChatResponse response = chatClient.call(prompt);
            String aiResponse = response.getResult().getOutput().getContent();

            log.info("AI 응답: {}", aiResponse);

            // JSON 파싱
            return parseAIResponse(aiResponse);

        } catch (Exception e) {
            log.error("회의록 분석 중 오류 발생", e);
            throw new RuntimeException("회의록 분석에 실패했습니다.", e);
        }
    }

    private MeetingAnalysisResponseDTO parseAIResponse(String aiResponse) {
        try {
            // JSON 부분만 추출 (```json으로 감싸진 경우 처리)
            String jsonResponse = aiResponse;
            if (aiResponse.contains("```json")) {
                jsonResponse = aiResponse.substring(
                        aiResponse.indexOf("```json") + 7,
                        aiResponse.lastIndexOf("```")
                ).trim();
            } else if (aiResponse.contains("```")) {
                jsonResponse = aiResponse.substring(
                        aiResponse.indexOf("```") + 3,
                        aiResponse.lastIndexOf("```")
                ).trim();
            }

            // JSON 파싱
            return objectMapper.readValue(jsonResponse, MeetingAnalysisResponseDTO.class);

        } catch (JsonProcessingException e) {
            log.error("AI 응답 파싱 실패: {}", aiResponse, e);

            // 파싱 실패 시 기본값 반환
            return MeetingAnalysisResponseDTO.builder()
                    .mainTopics(List.of("분석 실패"))
                    .decisions(List.of("분석 실패"))
                    .priorities(List.of("분석 실패"))
                    .build();
        }
    }
}
