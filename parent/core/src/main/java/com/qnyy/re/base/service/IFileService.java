package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.enums.FilePurposeEnum;
import com.qnyy.re.base.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * 文件服务
 * Created by E_Iva on 2017.11.30 0030.
 */
public interface IFileService {
    List<UploadFile> saveFile(FileTypeEnum fileType, FilePurposeEnum filePurpose, Long uid, Long objectId, MultipartFile... multipartFile) throws Exception;
    List<UploadFile> selectObjectFile(String filePurpose,Long objectId);

    List<UploadFile> queryFile(Collection<Long> ids);
}
