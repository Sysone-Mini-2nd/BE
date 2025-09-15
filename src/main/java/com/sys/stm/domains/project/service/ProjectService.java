package com.sys.stm.domains.project.service;

import com.sys.stm.domains.project.dto.request.ProjectCreateRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectListRequestDTO;
import com.sys.stm.domains.project.dto.request.ProjectUpdateRequestDTO;
import com.sys.stm.domains.project.dto.response.ProjectDetailResponseDTO;
import com.sys.stm.domains.project.dto.response.ProjectListResponseDTO;

public interface ProjectService {
    ProjectDetailResponseDTO getProject(Long projectId);
    ProjectListResponseDTO getProjectsByMemberId(Long memberId, ProjectListRequestDTO projectListRequestDTO);
    ProjectListResponseDTO getProjectsByMemberId(Long memberId);
    ProjectListResponseDTO getAllProject();
    ProjectDetailResponseDTO createProject(ProjectCreateRequestDTO projectCreateRequestDTO);
    ProjectDetailResponseDTO updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDTO);
    void deleteProject(Long projectId);
    ProjectDetailResponseDTO addProjectMember(Long projectId, Long memberId);
    void deleteProjectMember(Long projectId, Long memberId);
}
