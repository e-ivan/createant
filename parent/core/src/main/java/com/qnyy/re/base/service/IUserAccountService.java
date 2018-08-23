package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.UserAccount;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.entity.UserAccountStatement;
import com.qnyy.re.business.query.UserAccountStatementQueryObject;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/13.
 */
public interface IUserAccountService {
    UserAccount getUserAccountByUser(Long uid);
    void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type,Long objectId ,String content);
    void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type,Long objectId);
    void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type,String content);
    void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type);
    void updateSysAccount(BigDecimal amount, AccountUtil.AccountDealType type,Long objectId ,String content);
    PageResult<UserAccountStatement> queryUserAccountStatement(UserAccountStatementQueryObject qo);
    void updateUserAccount(Long uid, Long sourceUid, BigDecimal amount, AccountUtil.AccountDealType type, Long objectId ,String content);
}
