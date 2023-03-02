package project.bookservice.domain.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private Long id;
    private String title;
    private String description;
    private String image;
    public Book() {
    }

    public Book(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
