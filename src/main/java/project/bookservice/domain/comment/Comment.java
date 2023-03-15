package project.bookservice.domain.comment;


import lombok.Data;

@Data
public class Comment {

    private String content;
    private String bookisbn;
    private String writerName;
    private String writerID;
    private String date;
    private String starRating;


    public Comment(String content, String bookisbn, String writerName, String writerID, String date, String starRating) {
        this.content = content;
        this.bookisbn = bookisbn;
        this.writerName = writerName;
        this.writerID = writerID;
        this.date = date;
        this.starRating = starRating;
    }
}
