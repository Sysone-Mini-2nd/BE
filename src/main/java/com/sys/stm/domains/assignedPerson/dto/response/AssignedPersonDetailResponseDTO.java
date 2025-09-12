package com.sys.stm.domains.assignedPerson.dto.response;

import com.sys.stm.domains.assignedPerson.domain.AssignedPersonRole;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedPersonDetailResponseDTO {
    private Long id;
    private String name;
    private AssignedPersonRole role;
    private Timestamp lastLoginAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String picUrl;
    private String position;
}
