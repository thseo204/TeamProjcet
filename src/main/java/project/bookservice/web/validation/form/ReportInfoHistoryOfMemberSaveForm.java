package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReportInfoHistoryOfMemberSaveForm {

    private Long id;
    @NotBlank
    private String userId;
    @NotNull
    private Long reportId;
    private String writerId;
    @NotBlank
    private String isbn;
    @NotBlank
    private String title;
    private String uploadFileName;
    private String storeFileName;
    @NotBlank
    private String content;
    private String hashTag;
}
