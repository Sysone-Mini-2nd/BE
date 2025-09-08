package com.sys.stm.domains.tag.service;

import com.sys.stm.domains.tag.dao.TagRepository;
import com.sys.stm.domains.tag.dto.response.TagDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<TagDetailResponseDTO> getTags() {
        return tagRepository.findTags().stream()
                .map(tag -> TagDetailResponseDTO.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build())
                .toList();
    }
}
