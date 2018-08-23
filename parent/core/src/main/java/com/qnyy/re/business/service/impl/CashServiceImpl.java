package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.enums.AuditStatusEnum;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.Cash;
import com.qnyy.re.business.mapper.CashMapper;
import com.qnyy.re.business.service.ICashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/15.
 */
@Service
public class CashServiceImpl implements ICashService {

    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private CashMapper mapper;

    @Override
    public void addCash(BigDecimal amount) {
        if (amount == null){
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK,"amount");
        }
        UserInfo user = userInfoService.getByUid(UserContext.getUserId());
        if (!user.getBindCashAccount()){
            throw new BusinessException(CommonErrorResultEnum.CASH_ACCOUNT_UN_BIND);
        }
        if (user.getHasCashProcess()){
            throw new BusinessException(CommonErrorResultEnum.CASH_HAS_PROCESS);
        }
        //处理账户数据
        Cash c = new Cash();
        c.setAmount(amount);
        c.setNote(user.getNickname() + " 申请提现：" + amount + "元");
        c.setApplierId(user.getUid());
        c.setState(AuditStatusEnum.CREATE.getCode());
        mapper.insert(c);
        user.addState(BitStateUtil.OP_HAS_CASH_PROCESS);
        userInfoService.update(user);
        userAccountService.updateUserAccount(user.getUid(), amount, AccountUtil.AccountDealType.CASH_APPLY,c.getId());
    }

    @Override
    public void updateCashStatus(Long cid, boolean agree) {
        Cash cash = mapper.selectByPrimaryKey(cid);
        if (cash == null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        UserInfo user = userInfoService.getByUid(cash.getApplierId());
        if (user == null) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        if (!AuditStatusEnum.CREATE.getCode().equals(cash.getState())) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP);
        }
        cash.setState(agree ? AuditStatusEnum.PASS.getCode() : AuditStatusEnum.REFUSE.getCode());
        cash.setRemark("");
        cash.setAuditTime(new Date());
        cash.setAuditorId(null);
        this.update(cash);
        //处理账户数据
        if (agree){
            BigDecimal handleFee = cash.getAmount().multiply(SystemConstUtil.cashHandleFeeRatio).setScale(2,BigDecimal.ROUND_HALF_UP);
            BigDecimal amount = cash.getAmount().subtract(handleFee);
            userAccountService.updateUserAccount(cash.getApplierId(), amount,AccountUtil.AccountDealType.CASH_SUCCESSFUL,cid);
            userAccountService.updateUserAccount(cash.getApplierId(), handleFee,AccountUtil.AccountDealType.CASH_HANDLE_FEE,cid);
            userAccountService.updateSysAccount(amount, AccountUtil.AccountDealType.SYS_PARTNER_OUT,cid,cash.getApplierId().toString());
        }else {
            userAccountService.updateUserAccount(cash.getApplierId(),cash.getAmount(),AccountUtil.AccountDealType.CASH_REFUSE,cid);
        }
        user.removeState(BitStateUtil.OP_HAS_CASH_PROCESS);
        userInfoService.update(user);
    }

    @Override
    public void update(Cash cash) {
        if (mapper.updateByPrimaryKey(cash) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }
}
