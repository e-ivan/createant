package com.qnyy.re.business.service;

import com.qnyy.re.business.entity.Cash;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface ICashService {
    void addCash(BigDecimal amount);
    void updateCashStatus(Long cid, boolean agree);
    void update(Cash cash);
}
