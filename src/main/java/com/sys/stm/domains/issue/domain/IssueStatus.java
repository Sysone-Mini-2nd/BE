package com.sys.stm.domains.issue.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueStatus {
    TODO("할 일"),
    IN_PROGRESS("진행 중"),
    REVIEW("검토 중"),
    DONE("완료");

    private final String displayName;
}
