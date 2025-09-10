package com.sys.stm.domains.assignedPerson.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class AssignedPerson extends BaseEntity {
    private Long id;
    private Long projectId;
    private Long memberId;
    private AssignedPersonRole role;
}
