package project.bookservice.domain.report;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class ReportInfo {

    private String isbn; // 책의 key 값을 통해 도서 제목 가지고 오기
    private Long id;
    private String title;
    private String writerId; // 작성자 아이디를 통해 작성자의 닉네임 불러오기
//    private String bookTitle; // 작성한 도서 제목
//    private String reportTitle; // 게시글 제목
    private String date; // 작성일
//    private String fullPath;// 이미지 저장 경로
    private String uploadFileName; // 유저가 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명
    private String content; // 내용
    private Integer favoriteNum; // 좋아요 수
    private Integer collectionNum; //컬렉션에 저장된 수
    private Boolean disclosure; //공개여부
//    private Integer commentsNum; // 댓글 수
    private String hashTag;

//    private List<UploadFile> imageFiles; // 업로드한 이미지
    private UploadFile attachFile;
}
