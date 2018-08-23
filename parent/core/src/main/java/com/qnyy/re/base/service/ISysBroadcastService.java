package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.SysBroadcast;

import java.util.List;

/**
 * 系统广播服务
 * Created by E_Iva on 2018.1.8.0008.
 */
public interface ISysBroadcastService {
    void createBroadcast(SysBroadcast broadcast);

    /**
     * 获取系统广播
     */
    List<SysBroadcast> queryBroadcast();
}
