package com.sys.stm.domains.member.dto.request;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
/** 작성자: 김대호 */
@Data
public class MemberUpdateRequestDTO {
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;

    @Size(max = 255, message = "이름은 255자 이하여야 합니다")
    private String name;

    @Size(max = 255, message = "프로필 이미지 URL은 255자 이하여야 합니다")
    private String picUrl;

    @Size(max = 20, message = "직책은 20자 이하여야 합니다")
    private String position;

    @Size(min = 6, max = 255, message = "비밀번호는 6자 이상 255자 이하여야 합니다")
    private String password;

    // role 추가: 업데이트 시 역할 변경 허용(관리자 권한 통제는 서비스/컨트롤러에서 처리 권장)
    @Size(max = 10, message = "역할은 10자 이하여야 합니다")
    private String role;
}
