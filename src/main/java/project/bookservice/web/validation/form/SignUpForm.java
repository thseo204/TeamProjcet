package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 회원가입 시 각 Text 유효성 검사.
 */
@Data
public class SignUpForm {

    private Long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{5,20}$", message = "아이디는 5~20자내로 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
//    @Length(min =8, max = 20)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String userPwd;
    private String userRePwd;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
//    @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 숫자만 입력해주세요.")
    private String userPhone;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
//    @Pattern(regexp = "^[0-9]{6}$", message = "생년월일은 6자리로 숫자만 기입해주세요.")
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
