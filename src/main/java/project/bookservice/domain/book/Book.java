package project.bookservice.domain.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private Long id;
    private String imageUrl; // 이미지 링크
    private String author; //저자
    private String buyUrl; // 구매 링크
    private String publisher; //출판사
    private String description;
    private String title;
    private String pubDate; // 출판연도
    private String isbn; // 책의 key 값
    public Book() {
    }

    public Book(String imageUrl, String author, String buyUrl, String publisher, String description, String title, String pubDate, String isbn) {
        this.imageUrl = imageUrl;
        this.author = author;
        this.buyUrl = buyUrl;
        this.publisher = publisher;
        this.description = description;
        this.title = title;
        this.pubDate = pubDate;
        this.isbn = isbn;
    }
}
