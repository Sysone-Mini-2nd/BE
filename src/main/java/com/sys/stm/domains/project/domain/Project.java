package com.sys.stm.domains.project.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Project extends BaseEntity {
    private Long id;
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Timestamp startDate;
    private Timestamp endDate;
}
