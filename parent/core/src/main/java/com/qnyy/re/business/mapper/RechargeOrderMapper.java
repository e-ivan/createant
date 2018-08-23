package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.BaseOrder;
import com.qnyy.re.business.entity.RechargeOrder;
import java.util.List;

public interface RechargeOrderMapper {

    int insert(RechargeOrder record);

    RechargeOrder selectByPrimaryKey(Long id);

    RechargeOrder getByOrderId(String orderId);

    List<RechargeOrder> selectAll();

    int updateByPrimaryKey(BaseOrder record);
}