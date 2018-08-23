package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.InformantCenter;

import java.util.List;

public interface InformantCenterMapper {

    int insert(InformantCenter record);

    InformantCenter selectByPrimaryKey(Long id);

    List<InformantCenter> selectAll();

    int updateByPrimaryKey(InformantCenter record);
}