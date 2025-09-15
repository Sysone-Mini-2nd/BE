package com.sys.stm.domains.project.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    TODO("계획중"),
    IN_PROGRESS("진행 중"),
    DONE("완료"),
    PAUSED("정지");

    private final String displayName;
}
