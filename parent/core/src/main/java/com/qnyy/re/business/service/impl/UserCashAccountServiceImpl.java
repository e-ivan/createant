package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.ILoginInfoService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.MD5Util;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.UserCashAccount;
import com.qnyy.re.business.mapper.UserCashAccountMapper;
import com.qnyy.re.business.service.IUserCashAccountService;
import com.qnyy.re.business.vo.param.SaveCashAccountVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hh on 2017.11.6 0006.
 */
@Service
public class UserCashAccountServiceImpl implements IUserCashAccountService {
   @Autowired
   private UserCashAccountMapper mapper;
   @Autowired
   private IUserInfoService userInfoService;
   @Autowired
   private ILoginInfoService loginInfoService;

    @Override
    public void save(SaveCashAccountVO vo) {
        LoginInfo loginInfo = loginInfoService.getByUid(UserContext.getUserId());
        if (!MD5Util.encodePassword(vo.getPassword()).equals(loginInfo.getPassword())){
            throw new BusinessException(CommonErrorResultEnum.PASSWORD_ERROR);
        }
        UserCashAccount cashAccount = this.getCashAccountByUser(loginInfo.getUid());
        if (cashAccount != null){
            cashAccount.setRemark(vo.getRemark());
            cashAccount.setPhone(vo.getPhone());
            cashAccount.setAlipayAccount(vo.getAlipayAccount());
            cashAccount.setAccountName(vo.getAccountName());
            mapper.updateByPrimaryKey(cashAccount);
        }else {
            cashAccount = new UserCashAccount();
            cashAccount.setRemark(vo.getRemark());
            cashAccount.setPhone(vo.getPhone());
            cashAccount.setAlipayAccount(vo.getAlipayAccount());
            cashAccount.setAccountName(vo.getAccountName());
            cashAccount.setUid(UserContext.getUserId());
            mapper.insert(cashAccount);
            UserInfo user = userInfoService.getByUid(UserContext.getUserId());
            user.addState(BitStateUtil.OP_BIND_CASH_ACCOUNT);
            userInfoService.update(user);
        }
    }

    @Override
    public UserCashAccount getCashAccountByUser(Long uid) {
        return mapper.selectByUserId(uid);
    }
}
