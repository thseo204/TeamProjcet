package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BookmarkCollectionSaveForm {
    private Long id;
    @NotBlank
    private String userId;
    private String date;
    @NotBlank
    private String collectionName;

}
