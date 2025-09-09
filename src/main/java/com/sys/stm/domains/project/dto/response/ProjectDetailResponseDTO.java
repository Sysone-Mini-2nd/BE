package com.sys.stm.domains.project.dto.response;

import com.sys.stm.domains.project.domain.ProjectPriority;
import com.sys.stm.domains.project.domain.ProjectStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProjectDetailResponseDTO {
    private Long id;
    private String name;
    private String desc;
    private ProjectStatus status;
    private ProjectPriority priority;
    private Integer totalMemberCount;
    private Timestamp startDate;
    private Timestamp endDate;
    private String pmName;
    @Builder.Default
    List<Map<String, Object>> members = new ArrayList<>(); // 임시

//    @Builder.Default
//    List<Member> members = new ArrayList<>(); // 소속된 멤버 목록

}
