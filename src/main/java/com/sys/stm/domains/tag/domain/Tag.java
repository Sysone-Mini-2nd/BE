package com.sys.stm.domains.tag.domain;

import lombok.Builder;
import lombok.Data;
/** 작성자: 백승준 */
@Builder
@Data
public class Tag {
    private Long id;
    private String name;
}
