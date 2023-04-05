package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EditEmailForm {
    private String userId;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String newEmail;

    private String EmailCode;
}
