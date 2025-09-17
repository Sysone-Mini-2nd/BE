package com.sys.stm.domains.requirement.controller;

import com.sys.stm.domains.issue.dto.response.IssueMockResponseDTO;
import com.sys.stm.domains.requirement.service.RequirementService;
import com.sys.stm.global.common.response.ApiResponse;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.InvalidFileException;
import com.sys.stm.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/** 작성자: 백승준 */
@RestController
@RequestMapping("api/requirements")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService requirementService;

    @PostMapping("/upload")
    public ApiResponse<List<IssueMockResponseDTO>> uploadRequirements(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException(ExceptionMessage.FILE_NOT_SELECTED);
        }

        List<IssueMockResponseDTO> issues = requirementService.analyzeRequirements(file);

        return ApiResponse.ok(issues);
    }
}
