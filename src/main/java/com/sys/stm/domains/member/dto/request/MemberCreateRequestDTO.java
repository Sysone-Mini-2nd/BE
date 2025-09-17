package com.sys.stm.domains.member.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/** 작성자: 김대호 */
@Data
public class MemberCreateRequestDTO {
    @NotBlank(message = "계정 ID는 필수입니다")
    @Size(max = 255, message = "계정 ID는 255자 이하여야 합니다")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, max = 255, message = "비밀번호는 6자 이상 255자 이하여야 합니다")
    private String password;

    @NotBlank(message = "역할은 필수입니다")
    @Size(max = 10, message = "역할은 10자 이하여야 합니다")
    private String role;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다")
    private String email;

    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 255, message = "이름은 255자 이하여야 합니다")
    private String name;

    @Size(max = 255, message = "프로필 이미지 URL은 255자 이하여야 합니다")
    private String picUrl;

    @Size(max = 20, message = "직책은 20자 이하여야 합니다")
    private String position;
}
