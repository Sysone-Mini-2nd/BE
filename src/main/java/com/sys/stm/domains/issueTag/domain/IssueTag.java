package com.sys.stm.domains.issueTag.domain;

import lombok.Builder;
import lombok.Data;
/** 작성자: 백승준 */
@Data
@Builder
public class IssueTag {
    Long id;
    Long issueId;
    Long tagId;
}
