package com.sys.stm.domains.assignedPerson.dao;

import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDashBoardResponseDTO;
import com.sys.stm.domains.assignedPerson.dto.response.PmInfoResponseDTO;
import com.sys.stm.domains.assignedPerson.dto.response.AssignedPersonDetailResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssignedPersonRepository {
    int countByProjectId(Long projectId);
    int createAssignedPerson(AssignedPerson assignedPerson);
    void deleteByProjectIdAndMemberIds(@Param("projectId") Long projectId, @Param("memberIds") List<Long> memberIds);
    List<AssignedPersonDetailResponseDTO> findMembersByProjectId(Long projectId);
    List<Long> findMemberIdsByProjectId(Long projectId);
    List<PmInfoResponseDTO> findPmsByProjectIds(List<Long> projectIds);
    List<AssignedPersonDashBoardResponseDTO> findMembersNameByProjectId(Long projectId);
//    List<Member> findAllMemberByProjectId(Long projectId);
//    Member findPmByProjectId;
}
