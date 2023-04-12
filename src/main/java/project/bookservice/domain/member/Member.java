package project.bookservice.domain.member;

import lombok.Data;


@Data
public class Member {

    private String userId;
    private String userPwd;
    private String userRePwd;
    private String userName;
    private String userGender;
    private Integer userBirth;
    private String userEmail;
    private String emailCode;
    private String userPhone;
    private String userCharIcon;

    public Member() {
    }

    public Member(String userId, String userPwd, String userName, String userGender, Integer userBirth, String userEmail, String userPhone, String userCharIcon) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userGender = userGender;
        this.userBirth = userBirth;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userCharIcon = userCharIcon;
    }
}
