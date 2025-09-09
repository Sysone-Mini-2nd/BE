package com.sys.stm.domains.project.dao;

import com.sys.stm.domains.project.domain.Project;
import com.sys.stm.domains.project.dto.response.ProjectStatsResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProjectRepository {
    Project findById(Long projectId);
    List<Project> findAllByMemberId(Long memberId); // todo 관리자일 경우 전체 보기 처리
    int createProject(Project project);
    int updateProject(Project project);
    int deleteById(Long projectId);
    List<ProjectStatsResponseDTO> findProjectStatsByIds(@Param("projectIds") List<Long> projectIds);
}
