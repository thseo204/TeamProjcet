package project.bookservice.web.validation.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 독후기록 저장 시 각 Text 유효성 검사.
 */
@Data
public class ReportSaveForm {
    @NotBlank
    private String isbn; // 책의 key 값을 통해 도서 제목 가지고 오기

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String writerId; // 작성자 아이디를 통해 작성자의 닉네임 불러오기
    //    private String bookTitle; // 작성한 도서 제목
//    private String reportTitle; // 게시글 제목
    private String date; // 작성일
    //    private String fullPath;// 이미지 저장 경로
    @NotBlank
    private String uploadFileName; // 유저가 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명
    @NotBlank(message="기록을 남겨주세요.")
    private String content; // 내용
    private Integer favoriteNum; // 좋아요 수
    private Integer collectionNum; //컬렉션에 저장된 수
    @NotNull(message = "공개 여부를 선택해주세요.")
    private Boolean disclosure; //공개여부
    //    private Integer commentsNum; // 댓글 수
    private String hashTag;


}
