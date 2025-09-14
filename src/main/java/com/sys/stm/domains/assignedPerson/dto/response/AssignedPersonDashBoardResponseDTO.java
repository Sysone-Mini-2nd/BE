package com.sys.stm.domains.assignedPerson.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignedPersonDashBoardResponseDTO {
    private Long id;
    private String name;
    private String position;
}
