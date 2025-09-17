package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.issue.domain.IssuePriority;
import com.sys.stm.domains.issue.domain.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
/** 작성자: 배지원 */
@Data
@Builder
public class DashBoardIssuePriorityResponseDTO {                // 우선순위 작업
    private List<PriorityList> priority;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  PriorityList{
        private IssuePriority priority;
        private List<PriorityData> priorityDataList;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  PriorityData{
        private long id;
        private String title;
        private String writerName;
    }

}
