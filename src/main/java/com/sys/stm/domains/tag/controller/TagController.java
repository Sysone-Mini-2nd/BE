package com.sys.stm.domains.tag.controller;

import com.sys.stm.domains.tag.dto.response.TagDetailResponseDTO;
import com.sys.stm.domains.tag.service.TagService;
import com.sys.stm.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/** 작성자: 백승준 */
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("tags")
    public ApiResponse<List<TagDetailResponseDTO>> getTags() {
        return ApiResponse.ok(tagService.getTags());
    }
}
