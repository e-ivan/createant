package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UploadFile;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UploadFileMapper {

    int insert(UploadFile record);

    UploadFile selectByPrimaryKey(Long id);

    List<UploadFile> selectPurposeAll(@Param("purpose") String purpose, @Param("objectId") Long objectId);

    int updateByPrimaryKey(UploadFile record);

    List<UploadFile> selectBatch(Collection<Long> ids);
}