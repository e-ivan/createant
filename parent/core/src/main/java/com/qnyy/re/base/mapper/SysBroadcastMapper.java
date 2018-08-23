package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.SysBroadcast;

import java.util.List;

public interface SysBroadcastMapper {

    int insert(SysBroadcast record);

    SysBroadcast selectByPrimaryKey(Long id);

    List<SysBroadcast> selectAll();

    int updateByPrimaryKey(SysBroadcast record);
}