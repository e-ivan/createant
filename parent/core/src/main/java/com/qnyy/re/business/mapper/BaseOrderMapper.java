package com.qnyy.re.business.mapper;


import com.qnyy.re.business.entity.BaseOrder;

import java.util.List;

public interface BaseOrderMapper {

    int insert(BaseOrder record);

    BaseOrder selectByPrimaryKey(Long id);

    BaseOrder getByOrderId(String orderId);

    List<BaseOrder> selectAll();

    int updateByPrimaryKey(BaseOrder record);

    BaseOrder selectByOrderId(String selectByOrderId);
}