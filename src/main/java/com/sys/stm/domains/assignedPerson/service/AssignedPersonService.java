package com.sys.stm.domains.assignedPerson.service;

import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDashBoardResponseDTO;

import java.util.List;

public interface AssignedPersonService {
    List<AssignedPersonDashBoardResponseDTO> findMembersNameByProjectId(Long projectId);
    String findRoleByProjectIdAndMemberId(Long projectId, Long memberId);
}
