package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.entity.ReOrder;

public interface ReOrderMapper {

    int insert(ReOrder record);

    ReOrder selectByPrimaryKey(Long id);

    ReOrder getByOrderId(String orderId);

    int updateByPrimaryKey(BaseOrder record);
}