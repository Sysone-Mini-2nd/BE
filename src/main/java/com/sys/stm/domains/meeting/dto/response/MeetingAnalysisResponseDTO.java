package com.sys.stm.domains.meeting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingAnalysisResponseDTO {
    private List<String> mainTopics;        // 주요주제(주요 안건)
    private List<String> decisions;         // 의사 결정 사항
    private List<String> priorities;        // 우선순위 해야 할 일
    private List<String> recommends;        // 추천 업무
}