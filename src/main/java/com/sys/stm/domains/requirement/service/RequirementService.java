package com.sys.stm.domains.requirement.service;

import com.sys.stm.domains.issue.dto.response.IssueMockResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequirementService {
    List<IssueMockResponseDTO> analyzeRequirements(MultipartFile file);
}
