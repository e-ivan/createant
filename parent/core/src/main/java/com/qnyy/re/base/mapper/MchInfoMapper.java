package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.MchInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MchInfoMapper {

    int insert(MchInfo record);

    MchInfo selectByMchId(String mchId);

    List<MchInfo> selectAll();

    int updateApiPermissions(@Param("mchId") String mchId, @Param("apiPermissions") String apiPermissions);

    int updateByPrimaryKey(MchInfo record);
}