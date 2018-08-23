package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.enums.FilePurposeEnum;
import com.qnyy.re.base.enums.FileTypeEnum;
import com.qnyy.re.base.mapper.UploadFileMapper;
import com.qnyy.re.base.service.IFileService;
import com.qnyy.re.base.util.FileUploadUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by E_Iva on 2017.11.30 0030.
 */
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private UploadFileMapper mapper;
    private static boolean ZIP_FLAG; //判断是否用压缩图

    @Value("${imgFileCompress}")
    public void setZipFlag(boolean imgFileCompress) {
        FileServiceImpl.ZIP_FLAG = imgFileCompress;
    }

    @Override
    public List<UploadFile> saveFile(FileTypeEnum fileType, FilePurposeEnum filePurpose, Long uid, Long objectId, MultipartFile... multipartFile) throws Exception {
        List<UploadFile> list = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            UploadFile uploadFile = FileUploadUtils.saveFileInLocation(file, fileType, filePurpose, ZIP_FLAG);
            uploadFile.setUid(uid);
            uploadFile.setObjectId(objectId);
            //保存文件信息到数据库
            mapper.insert(uploadFile);
            list.add(uploadFile);
        }
        //返回数据
        return list;
    }

    @Override
    public List<UploadFile> selectObjectFile(String filePurpose, Long objectId) {
        return mapper.selectPurposeAll(filePurpose, objectId);
    }

    @Override
    public List<UploadFile> queryFile(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return mapper.selectBatch(ids);
    }

}
