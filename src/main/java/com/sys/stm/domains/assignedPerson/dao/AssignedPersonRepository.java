package com.sys.stm.domains.assignedPerson.dao;

import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AssignedPersonRepository {
    int countByProjectId(Long projectId);
    int createAssignedPerson(AssignedPerson assignedPerson);
    int deleteByProjectIdAndMemberIds(@Param("projectId") Long projectId, @Param("memberIds") List<Long> memberIds);
    List<Map<String, Object>> findMembersByProjectId(Long projectId); // todo: 반환 타입 List<Member>로 변환
    List<Long> findMemberIdsByProjectId(Long projectId);
//    List<Member> findAllMemberByProjectId(Long projectId);
//    Member findPmByProjectId;
}
