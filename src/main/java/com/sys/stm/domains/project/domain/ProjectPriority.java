package com.sys.stm.domains.project.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectPriority {
    LOW("낮음"),
    NORMAL("보통"),
    HIGH("높음");

    private final String displayName;
}
