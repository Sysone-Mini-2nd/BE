package com.sys.stm.domains.comment.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetailResponse {
    private Long id;
    private Long issueId;
    private Long parentId;
    private String content;
    private Long memberId;
    private String memberName;
    private String picUrl;
    private List<CommentDetailResponse> children = new ArrayList<>();
}
