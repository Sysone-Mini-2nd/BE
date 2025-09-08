package com.sys.stm.domains.assignedPerson.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignedPersonRole {
    USER("할 일"),
    PM("진행 중");

    private final String displayName;
}
