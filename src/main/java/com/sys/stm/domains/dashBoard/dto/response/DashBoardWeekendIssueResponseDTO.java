package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.issue.domain.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class DashBoardWeekendIssueResponseDTO {
    Map<Integer, WeekendIssueDTO> weekendIssue;



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeekendIssueDTO {
        long id;            // 이슈 번호
        String title;       // 이슈 제목
        Timestamp date;     // 이슈 마감일
        IssueStatus status; // 이슈 상태
    }
}
