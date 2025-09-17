package com.sys.stm.domains.tag.service;

import com.sys.stm.domains.tag.dto.response.TagDetailResponseDTO;

import java.util.List;
/** 작성자: 백승준 */
public interface TagService {
    List<TagDetailResponseDTO> getTags();
}
