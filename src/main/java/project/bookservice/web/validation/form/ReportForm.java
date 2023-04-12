package project.bookservice.web.validation.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 독후기록 저장 시 각 Text 유효성 검사.
 */
@Data
public class ReportForm {

    private String isbn; // 책의 key 값을 통해 도서 제목 가지고 오기
    private Long id;
    private String title;
    private String writerId;
    private String date; // 작성일
    private String uploadFileName; // 유저가 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명
    private MultipartFile attachFile;
    private String content; // 내용
    private Boolean disclosure; //공개여부
    private String hashTag;

}
