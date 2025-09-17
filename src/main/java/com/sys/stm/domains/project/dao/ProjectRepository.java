package com.sys.stm.domains.project.dao;

import com.sys.stm.domains.project.domain.Project;
import com.sys.stm.domains.project.dto.request.ProjectListRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectStatsResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/** 작성자: 백승준 */
@Mapper
public interface ProjectRepository {
    Project findById(Long projectId);
    List<Project> findAllByMemberId(
            @Param("memberId")Long memberId,
            @Param("dto") ProjectListRequestDTO projectListRequestDTO
    );
    List<Project> findAllByOnlyMemberId(
            @Param("memberId")Long memberId
    );
    int createProject(Project project);
    int updateProject(Project project);
    void deleteById(Long projectId);
    List<ProjectStatsResponseDTO> findProjectStatsByIds(@Param("projectIds") List<Long> projectIds);
    List<Project> findAllProject();
}
