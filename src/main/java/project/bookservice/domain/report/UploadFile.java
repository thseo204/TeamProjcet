package project.bookservice.domain.report;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName; // 유저가 업로드한 파일명
    private String storeFileName; // 서버 내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
