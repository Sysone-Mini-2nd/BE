package com.sys.stm.domains.dashBoard.service;

import com.sys.stm.domains.dashBoard.dto.response.DashBoardProjectListResponseDTO;
import com.sys.stm.domains.dashBoard.dto.response.DashBoardResponseDTO;

public interface DashBoardService {
    DashBoardResponseDTO findDashBoard(Long memberId, Long projectId);

    DashBoardProjectListResponseDTO getProjectsByMemberId(Long memberId);
}
