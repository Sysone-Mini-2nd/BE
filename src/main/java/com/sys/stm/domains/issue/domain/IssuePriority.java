package com.sys.stm.domains.issue.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssuePriority {
    LOW("낮음"),
    NORMAL("보통"),
    HIGH("높음"),
    WARNING("긴급");

    private final String displayName;
}
