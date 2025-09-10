package com.sys.stm.domains.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatsResponseDTO {
    private Long id;
    private Double progressRate;
    private Integer completedTasks;
    private Integer totalTasks;
    private Integer totalMemberCount;
}
