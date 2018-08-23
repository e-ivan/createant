package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.VersionUpgrade;
import org.springframework.web.multipart.MultipartFile;

/**
 * app版本服务
 * Created by E_Iva on 2018.1.17.0017.
 */
public interface IAppVersionService {
    void saveVersion(MultipartFile file,VersionUpgrade version);

    VersionUpgrade selectLatestVersion(byte appType,int versionCode);
}
