package com.sys.stm.domains.assignedPerson.dao;

import com.sys.stm.domains.assignedPerson.domain.AssignedPerson;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssignedPersonRepository {
    int countByProjectId(Long projectId);
    int createAssignedPerson(AssignedPerson assignedPerson);
//    List<Member> findAllMemberByProjectId(Long projectId);
//    Member findPmByProjectId;
}
