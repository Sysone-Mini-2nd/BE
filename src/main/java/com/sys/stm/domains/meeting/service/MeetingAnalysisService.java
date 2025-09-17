package com.sys.stm.domains.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.domains.meeting.dto.response.MeetingAnalysisResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/** 작성자: 배지원 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingAnalysisService {

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MeetingAnalysisResponseDTO analyzeMeetingContent(String meetingContent) {
        try {
            // 프롬프트 직접 생성 (PromptTemplate 사용하지 않음)
            String promptText = String.format("""
                다음 회의록 내용을 분석하여 다음 3가지로 분류해주세요:
                각 항목당 개수는 자유롭게 작성해줘 중요한것은 더 많이, 중요하지 않는 것은 좀 적게 나열
                
                1. 주요주제(주요 안건): 회의에서 논의된 주요 주제들을 나열
                2. 의사 결정 사항: 회의에서 결정된 사항들을 나열
                3. 우선순위 해야 할 일: 앞으로 해야 할 일들을 우선순위 순으로 자세하게 보충 설명과 함께 나열 (녹음파일에 날짜와 시간을 함께 지정하여 설명하였다면 
                해당 내용에 해당하는 날짜와 시간도 나열해줘 대신, 너가 추정해서 날짜와 시간은 작성하지 말아줘, 녹음파일에 시간과 날짜의 데이터가 존재하지 않다면
                날짜와 시간을 제외하고 나열해줘)
                4. 해야 할일의 모든 값들을 통해 추천업무를 작성해줘

                 
                회의록 내용:
                %s
                
                응답은 다음 JSON 형식으로만 제공해주세요:
                {
                    "mainTopics": ["주제1", "주제2", "주제3"],
                    "decisions": ["결정사항1", "결정사항2", "결정사항3"],
                    "priorities": ["할일1", "할일2", "할일3"],
                    "recommends" : ["추천업무1", "추천업무2", "추천업무3"]
                }
                """, meetingContent);

            // Prompt 직접 생성 (PromptTemplate 사용하지 않음)
            Prompt prompt = new Prompt(promptText);

            // OpenAI API 호출
            ChatResponse response = chatModel.call(prompt);
            String aiResponse = response.getResult().getOutput().getText();

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
                    .recommends(List.of("분석 실패"))
                    .build();
        }
    }
}
