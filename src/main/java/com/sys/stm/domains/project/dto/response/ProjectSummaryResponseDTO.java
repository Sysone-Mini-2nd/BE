package com.sys.stm.domains.project.dto.response;

import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDetailResponseDTO;
import com.sys.stm.domains.project.domain.ProjectPriority;
import com.sys.stm.domains.project.domain.ProjectStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryResponseDTO {
    private Long id;
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Double progressRate;     // 진행률(퍼센트)
    private Integer completedTasks;   // 완료된 작업 수
    private Integer totalTasks;       // 전체 작업 수
    private Integer totalMemberCount; // 전체 인원 수
    private Timestamp startDate;
    private Timestamp endDate;
    private String pmName;
    private Long pmId;
    @Builder.Default
    List<AssignedPersonDetailResponseDTO> members = new ArrayList<>();
}
