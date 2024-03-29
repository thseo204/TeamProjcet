package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class EditInformationForm {

    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
//    @Length(min =8, max = 20)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String userPwd;
    private String userRePwd;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String userPhone;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    private Integer userBirth;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String userGender;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z]{2,10}$", message = "이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String userName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userEmail;

    private String userCharIcon;
}
