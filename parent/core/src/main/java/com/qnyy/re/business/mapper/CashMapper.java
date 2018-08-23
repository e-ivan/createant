package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.Cash;

public interface CashMapper {

    int insert(Cash record);

    Cash selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Cash record);
}