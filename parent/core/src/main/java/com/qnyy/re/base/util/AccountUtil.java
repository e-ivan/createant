package com.qnyy.re.base.util;

import com.qnyy.re.base.entity.UserAccount;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 账户工具
 * Created by E_Iva on 2017.12.15.0015.
 */
public class AccountUtil {
    /**
     * 账户交易类型
     */
    @Getter
    @AllArgsConstructor
    public enum AccountDealType {
        CASH_APPLY(0, null, "申请提现", AccountArithmetic.USABLE_MINUS_FREEZE_ADD, AccountType.BALANCE, "申请提现", ""),
        CASH_SUCCESSFUL(1, null, "提现成功", AccountArithmetic.FREEZE_MINUS, AccountType.BALANCE, "提现成功", ""),
        CASH_REFUSE(2, null, "提现拒绝", AccountArithmetic.USABLE_ADD_FREEZE_MINUS, AccountType.BALANCE, "提现拒绝", ""),
        ORDER_PAY(3, null, "订单支付", AccountArithmetic.USABLE_MINUS, AccountType.BALANCE, "支付订单", ""),
        RECHARGE(4, null, "充值成功", AccountArithmetic.USABLE_ADD, AccountType.BALANCE, "充值", ""),
        DRAW_RED_ENVELOPE(5, 1005, "领取红包", AccountArithmetic.USABLE_ADD, AccountType.BALANCE, "领取", "的红包"),
        PAY_RED_ENVELOPE(6, 1006, "发红包", AccountArithmetic.USABLE_MINUS, AccountType.BALANCE, "发红包", ""),
        FIRST_RELATION_REAP(7, 1007, "一级推广收益", AccountArithmetic.USABLE_ADD, AccountType.BALANCE, "推广收益", ""),
        SECOND_RELATION_REAP(8, 1008, "二级推广收益", AccountArithmetic.USABLE_ADD, AccountType.BALANCE, "推广收益", ""),
        CASH_HANDLE_FEE(9, null, "提现手续费", AccountArithmetic.FREEZE_MINUS, AccountType.BALANCE, "提现成功，收取手续费", ""),
        SYS_RED_ENVELOPE_FEE(100, 1100, "红包手续费", AccountArithmetic.USABLE_ADD, AccountType.BALANCE, "收取用户ID", "红包手续费"),
        SYS_PARTNER_IN(200, null, "第三方支付", AccountArithmetic.FREEZE_ADD, AccountType.BALANCE, "用户ID", ""),
        SYS_PARTNER_OUT(201, null, "用户提现", AccountArithmetic.FREEZE_MINUS, AccountType.BALANCE, "用户ID", "申请提现成功"),;

        public static AccountDealType getByCode(Integer code) {
            for (AccountDealType accountDealType : AccountDealType.values()) {
                if (accountDealType.getCode().equals(code)) {
                    return accountDealType;
                }
            }
            return null;
        }

        public static Boolean getSymbol(Integer dealType) {
            AccountDealType type = getByCode(dealType);
            if (type != null) {
                AccountUtil.AccountArithmetic arithmetic = type.getArithmetic();
                if (AccountUtil.AccountArithmetic.USABLE_ADD.equals(arithmetic) ||
                        AccountUtil.AccountArithmetic.USABLE_ADD_FREEZE_MINUS.equals(arithmetic)) {
                    return true;
                } else if (AccountUtil.AccountArithmetic.USABLE_MINUS.equals(arithmetic) ||
                        AccountUtil.AccountArithmetic.USABLE_MINUS_FREEZE_ADD.equals(arithmetic)) {
                    return false;
                }
            }
            return null;
        }

        private Integer code;
        private Integer createantCode;
        private String typeName;
        private AccountArithmetic arithmetic;//账户算法
        private AccountType accountType;//账户类型
        private String remarkPre;//前缀
        private String remarkSuf;//后缀
    }

    /**
     * 账户类型
     */
    @Getter
    @AllArgsConstructor
    public enum AccountType {
        BALANCE("balance", "余额");

        private String code;
        private String value;
    }

    /**
     * 账户计算算法
     */
    @Getter
    @AllArgsConstructor
    public enum AccountArithmetic {
        USABLE_MINUS_FREEZE_ADD(0, false, true, "可用减，冻结加"),
        USABLE_ADD_FREEZE_MINUS(1, true, false, "可用加，冻结减"),
        USABLE_MINUS(2, false, null, "可用减"),
        USABLE_ADD(3, true, null, "可用加"),
        FREEZE_MINUS(4, null, false, "冻结减"),
        FREEZE_ADD(5, null, true, "冻结加");
        private Integer code;
        private Boolean usable;
        private Boolean freeze;
        private String value;
    }

    /**
     * 账户运算
     */
    public static UserAccount operationAccountAmount(UserAccount userAccount, BigDecimal amount, AccountDealType type) {
        AccountArithmetic arithmetic = type.getArithmetic();
        BigDecimal balance;
        BigDecimal freezeBalance;
        if (arithmetic.getUsable() != null) {
            if (arithmetic.getUsable()) {
                balance = userAccount.getBalance().add(amount);
            } else {
                if (AccountDealType.CASH_APPLY.equals(type)) {//如果是提现操作
                    BigDecimal maxCash = userAccount.getBalance().subtract(userAccount.getRefuseCash());
                    if (maxCash.compareTo(amount) < 0) {//且可提现金额不足
                        throw new BusinessException(CommonErrorResultEnum.CASH_AMOUNT_LACK);
                    }
                } else {
                    if (userAccount.getRefuseCash().compareTo(BigDecimal.ZERO) > 0) {//不可提现金额大于0时
                        BigDecimal refuse = userAccount.getRefuseCash().subtract(amount);
                        userAccount.setRefuseCash(refuse.compareTo(BigDecimal.ZERO) > 0 ? refuse : BigDecimal.ZERO);
                    }
                }
                balance = userAccount.getBalance().subtract(amount);
                if (balance.compareTo(BigDecimal.ZERO) < 0) {//账户余额不足
                    throw new BusinessException(CommonErrorResultEnum.BALANCE_LACK);
                }
            }
            userAccount.setBalance(balance);
        }
        if (arithmetic.getFreeze() != null) {
            if (arithmetic.getFreeze()) {
                freezeBalance = userAccount.getFreezeBalance().add(amount);
            } else {
                freezeBalance = userAccount.getFreezeBalance().subtract(amount);
                if (freezeBalance.compareTo(BigDecimal.ZERO) < 0) {//冻结金额异常
                    throw new BusinessException(CommonErrorResultEnum.ACCOUNT_ERROR, "账户当前冻结金额：" + userAccount.getFreezeBalance() + "元");
                }
            }
            userAccount.setFreezeBalance(freezeBalance);
        }
        return userAccount;
    }

}
