package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class KeywordSaveForm {
    private Long id;
    @NotBlank
    private String keyword;
    @NotBlank
    private String isbn;
    @NotNull
    private Long reportId;
}
