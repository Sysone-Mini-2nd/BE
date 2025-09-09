package com.sys.stm.domains.assignedPerson.dao;

import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssignedPersonRepository {
    int countByProjectId(Long projectId);
    int createAssignedPerson(AssignedPerson assignedPerson);
    List<Map<String, Object>> findMembersByProjectId(Long projectId); // todo: 반환 타입 List<Member>로 변환
//    List<Member> findAllMemberByProjectId(Long projectId);
//    Member findPmByProjectId;
}
