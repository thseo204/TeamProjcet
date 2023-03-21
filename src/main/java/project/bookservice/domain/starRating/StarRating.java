package project.bookservice.domain.starRating;

import lombok.Data;

@Data
public class StarRating {
    private String userId;
    private String isbn;
    private Double starRating;

    public StarRating(String userId, String isbn, Double starRating) {
        this.userId = userId;
        this.isbn = isbn;
        this.starRating = starRating;
    }
}
