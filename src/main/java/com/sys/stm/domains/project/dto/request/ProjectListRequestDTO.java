package com.sys.stm.domains.project.dto.request;

import com.sys.stm.domains.project.domain.ProjectPriority;
import com.sys.stm.domains.project.domain.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListRequestDTO {
    private ProjectPriority priority; // 우선순위
    private ProjectStatus status;   // 상태
    private String search;          // 검색어
    private ProjectSortBy sortBy;   // 정렬 기준
}
