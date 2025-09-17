package com.sys.stm.domains.project.dto.request;

import com.sys.stm.domains.project.domain.ProjectPriority;
import com.sys.stm.domains.project.domain.ProjectStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequestDTO {
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Timestamp startDate;
    private Timestamp endDate;
    @Builder.Default
    List<Long> memberIds = new ArrayList<>();
    Long pmId;
}
