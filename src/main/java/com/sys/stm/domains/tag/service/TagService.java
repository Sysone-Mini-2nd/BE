package com.sys.stm.domains.tag.service;

import com.sys.stm.domains.tag.dto.response.TagDetailResponseDTO;

import java.util.List;

public interface TagService {
    List<TagDetailResponseDTO> getTags();
}
