package project.bookservice.domain.comment;


import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String content;
    private String bookisbn;
    private String writerID;
    private String date;
    private String charIcon;

    public Comment() {
    }

    public Comment(String writerID, String charIcon) {
        this.writerID = writerID;
        this.charIcon = charIcon;
    }

    public Comment(Long id, String content, String bookisbn, String writerID, String date, String charIcon) {
        this.id = id;
        this.content = content;
        this.bookisbn = bookisbn;
        this.writerID = writerID;
        this.date = date;
        this.charIcon = charIcon;
    }
}
