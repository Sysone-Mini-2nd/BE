package com.sys.stm.domains.assignedPerson.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignedPersonRole {
    USER("팀원"),
    PM("프로젝트 매니저");

    private final String displayName;
}
