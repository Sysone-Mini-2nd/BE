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
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponseDTO {
    private Long id;
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Integer totalMemberCount;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long pmId;
    private String pmName;
    @Builder.Default
    List<AssignedPersonDetailResponseDTO> members = new ArrayList<>(); // 소속된 멤버 목록

}
