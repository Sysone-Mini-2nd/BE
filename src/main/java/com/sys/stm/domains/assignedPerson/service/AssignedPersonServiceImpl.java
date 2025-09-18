package com.sys.stm.domains.assignedPerson.service;

import com.sys.stm.domains.assignedPerson.dao.AssignedPersonRepository;
import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDashBoardResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/** 작성자: 백승준 */
@Service
@RequiredArgsConstructor
@Transactional
public class AssignedPersonServiceImpl implements AssignedPersonService {
    private final AssignedPersonRepository assignedPersonRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AssignedPersonDashBoardResponseDTO> findMembersNameByProjectId(Long projectId) {
        return assignedPersonRepository.findMembersNameByProjectId(projectId);
    }

    @Override
    public String findRoleByProjectIdAndMemberId(Long projectId, Long memberId) {
        return assignedPersonRepository.findRoleByProjectIdAndMemberId(projectId, memberId);
    }


}
