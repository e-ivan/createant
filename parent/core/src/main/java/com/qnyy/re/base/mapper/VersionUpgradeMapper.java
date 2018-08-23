package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.VersionUpgrade;
import org.apache.ibatis.annotations.Param;

public interface VersionUpgradeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VersionUpgrade record);

    int updateByPrimaryKey(VersionUpgrade record);

    VersionUpgrade selectLatestVersion(byte appType);

    int queryBeforeVersionMustUpgrade(@Param("appType") byte appType, @Param("versionCode") int versionCode);
}