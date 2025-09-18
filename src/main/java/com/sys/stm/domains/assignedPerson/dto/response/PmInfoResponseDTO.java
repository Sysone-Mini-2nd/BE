package com.sys.stm.domains.assignedPerson.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/** 작성자: 백승준 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmInfoResponseDTO {
    private Long projectId;
    private Long pmId;
    private String pmName;
}
