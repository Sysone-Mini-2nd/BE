package com.sys.stm.domains.assignedPerson.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssignedPersonRepository {
    int countByProjectId(Long projectId);
//    List<Member> findAllMemberByProjectId(Long projectId);
//    Member findPmByProjectId;
}
