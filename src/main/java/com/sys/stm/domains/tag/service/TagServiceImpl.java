package com.sys.stm.domains.tag.service;

import com.sys.stm.domains.tag.dao.TagRepository;
import com.sys.stm.domains.tag.dto.response.TagDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<TagDetailResponse> getTags() {
        return tagRepository.findTags().stream()
                .map(tag -> TagDetailResponse.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build())
                .toList();
    }
}
