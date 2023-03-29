package project.bookservice.domain.naverUser;

import lombok.Data;

@Data
public class NaverUser {
    private String naverUserId;
    private String naverUserNickname;

    public NaverUser(){

    }

    public NaverUser(String naverUserId, String naverUserNickname) {
        this.naverUserId = naverUserId;
        this.naverUserNickname = naverUserNickname;
    }
}
