package com.sys.stm.domains.dashBoard.dto.response;

import com.sys.stm.domains.project.dto.response.ProjectSummaryResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DashBoardProjectListResponseDTO {              // 프로젝트 리스트
    private int projectCount;
    private int issueCount;
    @Builder.Default
    private List<DashBoardProjectDataResponseDTO> projects = new ArrayList<>();
}
