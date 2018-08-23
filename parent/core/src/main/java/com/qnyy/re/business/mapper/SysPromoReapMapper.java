package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.SysPromoReap;
import java.util.List;

public interface SysPromoReapMapper {

    int insert(SysPromoReap record);

    SysPromoReap selectByPrimaryKey(Long id);

    SysPromoReap selectLatest();

    List<SysPromoReap> selectAll();

    int updateByPrimaryKey(SysPromoReap record);
}