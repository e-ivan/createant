package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserAccount;
import com.qnyy.re.base.entity.UserAccountStatement;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.mapper.UserAccountStatementMapper;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.query.UserAccountStatementQueryObject;
import com.qnyy.re.business.util.CreateantRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
@Service
public class UserAccountServiceImpl implements IUserAccountService {

    @Resource
    private UserAccountStatementMapper userAccountStatementMapper;
    @Resource
    private LoginInfoMapper loginInfoMapper;

    private String getCreateantToken(Long uid){
        //获取uid对应的创蚁id
        LoginInfo loginInfo = loginInfoMapper.selectByPrimaryKey(uid);
        if (loginInfo == null || StringUtils.isBlank(loginInfo.getCreateantToken())) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        return loginInfo.getCreateantToken();
    }

    public UserAccount getUserAccountByUser(Long uid) {
        //获取创蚁中的账户
        String createantToken = this.getCreateantToken(uid);
        UserAccount userAccount = CreateantRequestUtil.getUserAccount(createantToken);
        if (userAccount == null) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        userAccount.setUid(uid);
        return userAccount;
    }

    @Override
    public void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type, Long objectId ,String content) {
        this.updateUserAccount(uid, null,amount, type, objectId, content);
    }

    /**
     * 添加流水信息
     */
    private UserAccountStatement saveStatement(UserAccount userAccount, BigDecimal amount, AccountUtil.AccountDealType type,Long objectId , String content){
        UserAccountStatement uas = new UserAccountStatement();
        uas.setAccountUser(userAccount.getUid());
        uas.setDealType(type.getCode());
        uas.setAmount(amount);
        uas.setUsableAmount(userAccount.getBalance());
        uas.setFreezeAmount(userAccount.getFreezeBalance());
        uas.setAccountType(type.getAccountType().getCode());
        uas.setObjectId(objectId);
        uas.setRemark(type.getRemarkPre() + content + type.getRemarkSuf());
        Boolean inOrOut;
        if (type.getArithmetic().getUsable() != null) {
            inOrOut = type.getArithmetic().getUsable();
        }else {
            inOrOut = type.getArithmetic().getFreeze();
        }
        uas.setInOrOut(inOrOut);
        userAccountStatementMapper.insert(uas);
        return uas;
    }

    @Override
    public void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type) {
        this.updateUserAccount(uid, amount, type,null,"");
    }

    @Override
    public void updateSysAccount(BigDecimal amount, AccountUtil.AccountDealType type, Long objectId, String content) {
        this.updateUserAccount(SystemConstUtil.systemAccountId,amount,type,objectId,content);
    }

    @Override
    public void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type, Long objectId) {
        this.updateUserAccount(uid, amount, type,objectId,"");
    }

    @Override
    public void updateUserAccount(Long uid, BigDecimal amount, AccountUtil.AccountDealType type, String content) {
        this.updateUserAccount(uid, amount, type,null,content);
    }

    private void update(UserAccountStatement userAccountStatement, Integer version, Long sourceUid) {
        String createantToken = this.getCreateantToken(userAccountStatement.getAccountUser());
        String sourceCreateantToken = null;
        if (sourceUid != null) {
            sourceCreateantToken = this.getCreateantToken(sourceUid);
        }
        CreateantRequestUtil.updateUserAccount(createantToken,sourceCreateantToken,version,userAccountStatement);
    }

    @Override
    public PageResult<UserAccountStatement> queryUserAccountStatement(UserAccountStatementQueryObject qo) {
        int count = userAccountStatementMapper.queryCount(qo);
        List<UserAccountStatement> list = userAccountStatementMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void updateUserAccount(Long uid, Long sourceUid, BigDecimal amount, AccountUtil.AccountDealType type, Long objectId, String content) {
        if (amount.compareTo(BigDecimal.ZERO) < 0){
            throw new BusinessException(CommonErrorResultEnum.AMOUNT_ERROR);
        }else if (amount.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        UserAccount userAccount = this.getUserAccountByUser(uid);
        AccountUtil.operationAccountAmount(userAccount, amount, type);
        //更新账户
        UserAccountStatement uas = this.saveStatement(userAccount, amount, type, objectId, content);
        this.update(uas,userAccount.getVersion(), sourceUid);
    }


}
