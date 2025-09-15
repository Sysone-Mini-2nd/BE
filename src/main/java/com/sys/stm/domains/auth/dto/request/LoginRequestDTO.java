package com.sys.stm.domains.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "계정 ID는 필수입니다")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
