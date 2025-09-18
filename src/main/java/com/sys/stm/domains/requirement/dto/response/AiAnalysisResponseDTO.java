package com.sys.stm.domains.requirement.dto.response;

import com.sys.stm.domains.issue.dto.response.IssueMockResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/** 작성자: 백승준 */
@Getter
@Setter
@NoArgsConstructor
public class AiAnalysisResponseDTO {

    private List<IssueMockResponseDTO> issues;
}
