package com.qnyy.re.base.vo;

import com.qnyy.re.base.enums.FilePurposeEnum;
import com.qnyy.re.base.enums.FileTypeEnum;
import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.container.BaseParamVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by E_Iva on 2018.6.25 0025.
 */
@Getter@Setter
public class UploadFileVO extends BaseParamVO{
    @VerifyParam
    private FileTypeEnum fileType;   //文件类型
    @VerifyParam
    private FilePurposeEnum filePurpose;//文件用途
    private Long uid;
    private Long objectId;
    @VerifyParam("file")
    private MultipartFile[] file;
    @VerifyParam("file")
    private String base64;

    public void setFileType(String fileType) {
        try {
            this.fileType = FileTypeEnum.valueOf(fileType);
        } catch (IllegalArgumentException e) {
            this.fileType = null;
        }
    }

    public void setFilePurpose(String filePurpose) {
        try {
            this.filePurpose = FilePurposeEnum.valueOf(filePurpose);
        } catch (IllegalArgumentException e) {
            this.filePurpose = null;
        }
    }

}
