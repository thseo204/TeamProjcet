package project.bookservice.web.validation.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 독후기록 저장 시 각 Text 유효성 검사.
 */
@Data
public class ReportForm {
    @NotBlank
    private String isbn; // 책의 key 값을 통해 도서 제목 가지고 오기

    private Long id;
    private String title;

    @NotBlank
    private String writerId;

//    @NotBlank(message = "아이디는 필수 입력 값입니다.")
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{5,20}$", message = "아이디는 5~20자내로 입력해주세요.")
//    private String userId;


    private String date; // 작성일

//    private List<MultipartFile> imageFiles; // 업로드한 이미지
    private String uploadFileName; // 유저가 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명
    private MultipartFile attachFile;
    @NotBlank(message="기록을 남겨주세요.")
    private String content; // 내용

    @NotNull(message = "공개 여부를 선택해주세요.")
    private Boolean disclosure; //공개여부


    private String hashTag;

}
