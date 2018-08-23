package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.MchInfo;

import java.util.List;

/**
 * 商户服务
 * Created by E_Iva on 2018.2.8.0008.
 */
public interface IMchService {
    void createMch(MchInfo info);

    void setMchApiPermissions(String mchId, List<Long> apiIds);

    MchInfo getByMchId(String mchId);

    /**
     * 初始化本地系统mch
     */
    void iniSystemMch();
}
