package com.sys.stm.domains.member.dto.response;

import lombok.Data;

import java.sql.Timestamp;
/** 작성자: 김대호 */
@Data
public class MemberResponseDTO {
    private Long id;
    private String accountId;
    private String role;
    private String email;
    private String name;
    private Timestamp lastLoginAt;
    private String picUrl;
    private String position;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
