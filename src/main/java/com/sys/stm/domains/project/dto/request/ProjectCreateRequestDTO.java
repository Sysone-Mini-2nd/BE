package com.sys.stm.domains.project.dto.request;

import com.sys.stm.domains.issue.dto.request.IssueCreateRequestDTO;
import com.sys.stm.domains.project.domain.ProjectPriority;
import com.sys.stm.domains.project.domain.ProjectStatus;
import java.sql.Timestamp;
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
public class ProjectCreateRequestDTO {
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long pmId;
    private List<Long> memberIds;
    List<IssueCreateRequestDTO> issues;
}
