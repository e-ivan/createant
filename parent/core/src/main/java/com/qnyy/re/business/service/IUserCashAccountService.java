package com.qnyy.re.business.service;

import com.qnyy.re.business.entity.UserCashAccount;
import com.qnyy.re.business.vo.param.SaveCashAccountVO;

/**
 * 用户提现账户服务
 * Created by hh on 2017.11.6 0006.
 */
public interface IUserCashAccountService {
    /**
     * 保存用户提现账户
     */
    void save(SaveCashAccountVO vo);

    //根据用户查找订单账户
    UserCashAccount getCashAccountByUser(Long uid);
}
