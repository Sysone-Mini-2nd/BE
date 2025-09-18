package com.sys.stm.domains.requirement.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
/** 작성자: 백승준 */
@Component
@RequiredArgsConstructor
public class AiClient {
    private final ChatModel chatModel;

    public String analyzeRequirements(String jsonData) {
        String template = """
                지금 제공하는 문서는 요구사항 문서입니다. 다음 작업을 수행해주세요:
                
                1. 문서 분석: 요구사항 정의서의 내용을 상세히 분석하여 주요 과업들을 식별해주세요.
                2. 과업 분류: 사용자가 과업을 효과적으로 분류할 수 있도록 각 요구사항을 제목과 내용 단위로 정리해주세요.
                3. 중복/누락 분석: 다음 사항들을 검토하고 판단하여 분류해주세요:
                    - 요구사항 간 겹치는 부분이 있는지 확인
                    - 요구사항 정의서에는 없지만 필요한 항목들이 있는지 검토
                    - 별도로 나뉘어 있지만 하나로 합쳐도 될 항목들이 있는지 판단
                
                4. 결과 형식: 아래 JSON 형식으로 결과를 제공해주세요:
                
                {
                    "issues": [
                        {
                            "title": "제목1",
                            "desc": "설명1"
                        },
                        {
                            "title": "제목2",
                            "desc": "설명2"
                        },
                        {
                            "title": "제목3",
                            "desc": "설명3"
                        }
                    ]
                }
                
                주의: desc에 대한 내용을 깔끔하게 바꾸되, 내용에 대한 별도의 조언이나 훈수는 하지 마세요.
                """;
        Prompt prompt = new Prompt(template + jsonData);

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }
}
