package com.sys.stm.domains.project.dto.response;

import com.sys.stm.domains.project.domain.ProjectStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListResponseDTO {
    @Builder.Default
    private List<ProjectSummaryResponseDTO> projects = new ArrayList<>();
    private Integer total;
    @Builder.Default
    private Map<ProjectStatus, Integer> statusCounts = new HashMap<>();
    private Integer delayed;
}
