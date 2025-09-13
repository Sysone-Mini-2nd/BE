package com.sys.stm.domains.member.domain;
import com.sys.stm.global.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class Member extends BaseEntity {
    private Long id;
    private String accountId;
    private String password;
    private String role;
    private String email;
    private String name;
    private Timestamp lastLoginAt;
    private Integer isDeleted;  // Boolean -> Integer로 변경
    private String picUrl;
    private String position;
}

