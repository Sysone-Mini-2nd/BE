package com.sys.stm.domains.messenger.domain;

import com.sys.stm.global.common.entity.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@Builder
//TODO Member 없어서 임시로 만든 클래스
public class Member extends BaseEntity {
    private Long id;
    private String accountId;
    private String password;
    private String role;
    private String email;
    private String name;
    private LocalDateTime lastLoginAt;
    private Integer isDeleted;
    private String picUrl;
    private String position;

}
