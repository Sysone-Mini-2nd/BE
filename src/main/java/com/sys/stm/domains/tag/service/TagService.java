package com.sys.stm.domains.tag.service;

import com.sys.stm.domains.tag.dto.response.TagDetailResponse;

import java.util.List;

public interface TagService {
    List<TagDetailResponse> getTags();
}
