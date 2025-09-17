package com.sys.stm.domains.dashBoard.service;

import com.sys.stm.domains.dashBoard.dto.response.DashBoardProjectListResponseDTO;
import com.sys.stm.domains.dashBoard.dto.response.DashBoardResponseDTO;
/** 작성자: 배지원 */
public interface DashBoardService {
    DashBoardResponseDTO findDashBoard(Long memberId, Long projectId, String userRole);

    DashBoardProjectListResponseDTO getProjectsByMemberId(Long memberId, String memberRole);
}
