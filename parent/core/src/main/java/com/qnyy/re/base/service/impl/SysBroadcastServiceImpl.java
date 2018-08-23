package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.SysBroadcast;
import com.qnyy.re.base.mapper.SysBroadcastMapper;
import com.qnyy.re.base.service.ISysBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by E_Iva on 2018.1.8.0008.
 */
@Service
public class SysBroadcastServiceImpl implements ISysBroadcastService {
    @Autowired
    private SysBroadcastMapper mapper;
    @Override
    public void createBroadcast(SysBroadcast broadcast) {
        SysBroadcast sysBroadcast = new SysBroadcast();
        sysBroadcast.setType(0);
        sysBroadcast.setUrl(broadcast.getUrl());
        sysBroadcast.setLocation(broadcast.getLocation());
        sysBroadcast.setTitle(broadcast.getTitle());
        sysBroadcast.setSequence(broadcast.getSequence());
        mapper.insert(broadcast);
    }

    @Override
    public List<SysBroadcast> queryBroadcast() {
        return mapper.selectAll();
    }
}
