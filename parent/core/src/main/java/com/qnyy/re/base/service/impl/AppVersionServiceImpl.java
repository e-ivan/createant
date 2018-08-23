package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.VersionUpgrade;
import com.qnyy.re.base.enums.FileTypeEnum;
import com.qnyy.re.base.mapper.VersionUpgradeMapper;
import com.qnyy.re.base.service.IAppVersionService;
import com.qnyy.re.base.service.IFileService;
import com.qnyy.re.base.util.SystemConstUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by E_Iva on 2018.1.17.0017.
 */
@Service
public class AppVersionServiceImpl implements IAppVersionService {
    @Autowired
    private VersionUpgradeMapper mapper;
    @Autowired
    private IFileService fileService;
    @Override
    public void saveVersion(MultipartFile file, VersionUpgrade version) {
        mapper.insert(version);
        try {
            List<UploadFile> files = fileService.saveFile(FileTypeEnum.APP, null, SystemConstUtil.systemAccountId, version.getId(), file);
            version.setApkUrl(files.get(0).getPath());
            mapper.updateByPrimaryKey(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VersionUpgrade selectLatestVersion(byte appType,int versionCode) {
        VersionUpgrade versionUpgrade = mapper.selectLatestVersion(appType);
        if (versionUpgrade != null && !versionUpgrade.isMust()) {
            versionUpgrade.setMust(mapper.queryBeforeVersionMustUpgrade(appType,versionCode) > 0);
        }
        return versionUpgrade;
    }
}
