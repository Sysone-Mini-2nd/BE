package com.sys.stm.domains.project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/** 작성자: 백승준 */
@Getter
@RequiredArgsConstructor
public enum ProjectSortBy {
    CREATED_AT("생성일순"),
    NAME("이름순"),
    END_DATE("마감일순"),
    PRIORITY("우선순위순");

    private final String displayName;
}
