package com.sys.stm.domains.requirement.service;

import com.sys.stm.domains.issue.dto.response.IssueMockResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/** 작성자: 백승준 */
public interface RequirementService {
    List<IssueMockResponseDTO> analyzeRequirements(MultipartFile file);
}
