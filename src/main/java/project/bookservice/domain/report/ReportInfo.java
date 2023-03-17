package project.bookservice.domain.report;

import lombok.Data;

import java.util.Date;

@Data
public class ReportInfo {

    private String isbn; // 책의 key 값을 통해 도서 제목 가지고 오기
    private Long id;
    private String userId; // 작성자 아이디를 통해 작성자의 닉네임 불러오기
//    private String bookTitle; // 작성한 도서 제목
    private String reportTitle; // 게시글 제목
    private Date uploadDate; // 작성일
    private String uploadImage; // 업로드한 이미지
    private String report;
    private Integer favoriteNum; // 좋아요 수
    private Integer saveToCollectionNum; //컬렉션에 저장된 수
//    private Integer commentsNum; // 댓글 수

}
