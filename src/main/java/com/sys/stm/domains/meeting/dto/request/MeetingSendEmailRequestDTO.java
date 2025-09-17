package com.sys.stm.domains.meeting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/** 작성자: 배지원 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingSendEmailRequestDTO {
    private List<String> to;           // 받는 사람 이메일
    private String subject;      // 제목
    private String content;      // 내용
}
