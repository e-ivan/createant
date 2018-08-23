package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.ShakearoundPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShakearoundPageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShakearoundPage record);

    ShakearoundPage selectByPrimaryKey(Long id);

    List<ShakearoundPage> selectAll();

    int updateByPrimaryKey(ShakearoundPage record);

    ShakearoundPage selectByCondition(@Param("storeId") Long storeId, @Param("uid")Long uid);
}