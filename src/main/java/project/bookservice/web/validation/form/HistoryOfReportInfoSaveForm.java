package project.bookservice.web.validation.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HistoryOfReportInfoSaveForm {
    private Long id;
    @NotNull
    private Long reportId;

    @NotBlank
    private String userId;
    private Boolean favorite;
    private Boolean collection;

}
