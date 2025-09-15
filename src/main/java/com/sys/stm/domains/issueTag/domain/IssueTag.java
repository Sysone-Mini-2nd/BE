package com.sys.stm.domains.issueTag.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueTag {
    Long id;
    Long issueId;
    Long tagId;
}
