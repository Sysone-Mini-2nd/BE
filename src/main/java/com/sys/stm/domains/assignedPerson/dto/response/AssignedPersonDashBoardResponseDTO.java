package com.sys.stm.domains.assignedPerson.dto.response;

import lombok.Builder;
import lombok.Data;
/** 작성자: 백승준 */
@Data
@Builder
public class AssignedPersonDashBoardResponseDTO {
    private Long id;
    private String name;
    private String position;
}
