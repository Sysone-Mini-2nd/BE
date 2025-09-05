package com.sys.stm.domains.tag;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Tag {
    private Long id;
    private String name;
}
