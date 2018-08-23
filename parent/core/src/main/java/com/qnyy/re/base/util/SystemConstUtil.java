package com.qnyy.re.base.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 系统配置变量
 * Created by E_Iva on 2018.1.8.0008.
 */
@Component
public class SystemConstUtil {
    public static final String ASSIGN_STR = "5be64161c90ac0925bf028f3c8291bca";

    public static BigDecimal reMomentFeeRatio;

    public static boolean productionState;

    public static boolean checkMch;

    public static boolean checkTime;

    public static boolean checkSign;

    public static String fileRootPath;

    public static String fileLogicRoot;

    public static String userDefaultIntro;

    public static String userDefaultHeadUrl;

    public static Integer oneUserShakeReDrawTime;

    public static BigDecimal cashHandleFeeRatio;

    public static String userGeneralizeUrl;

    public static Long systemAccountId;

    public static String systemAccountUsername;
    public static String systemCreateantToken;

    public static String systemMchId;
    public static String systemMchName;

    public static String wechatNotifyUrl;
    public static String wechatPayAppId;
    public static String wechatPayApiKey;
    public static String wechatPayMchId;

    @Value("${wechatNotifyUrl}")
    public void setWechatNotifyUrl(String wechatNotifyUrl) {
        SystemConstUtil.wechatNotifyUrl = wechatNotifyUrl;
    }

    @Value("${wechatPayAppId}")
    public void setWechatPayAppId(String wechatPayAppId) {
        SystemConstUtil.wechatPayAppId = wechatPayAppId;
    }

    @Value("${wechatPayApiKey}")
    public void setWechatPayApiKey(String wechatPayApiKey) {
        SystemConstUtil.wechatPayApiKey = wechatPayApiKey;
    }

    @Value("${wechatPayMchId}")
    public void setWechatPayMchId(String wechatPayMchId) {
        SystemConstUtil.wechatPayMchId = wechatPayMchId;
    }

    @Value("${systemAccountUsername}")
    public void setSystemAccountUsername(String systemAccountUsername) {
        SystemConstUtil.systemAccountUsername = systemAccountUsername;
    }

    @Value("${systemCreateantToken}")
    public void setSystemCreateantToken(String systemCreateantToken) {
        SystemConstUtil.systemCreateantToken = systemCreateantToken;
    }

    @Value("${systemMchId}")
    public void setSystemMchId(String systemMchId) {
        SystemConstUtil.systemMchId = systemMchId;
    }
    @Value("${systemMchName}")
    public void setSystemMchName(String systemMchName) {
        SystemConstUtil.systemMchName = systemMchName;
    }


    @Value("${systemAccountId}")
    public void setSystemAccountId(Long systemAccountId) {
        SystemConstUtil.systemAccountId = systemAccountId;
    }

    @Value("${userGeneralizeUrl}")
    public void setUserGeneralizeUrl(String userGeneralizeUrl) {
        SystemConstUtil.userGeneralizeUrl = userGeneralizeUrl;
    }

    @Value("${cashHandleFeeRatio}")
    public void setCashHandleFeeRatio(BigDecimal cashHandleFeeRatio) {
        if (cashHandleFeeRatio.compareTo(BigDecimal.ZERO) < 0) {
            cashHandleFeeRatio = BigDecimal.ZERO;
        }
        SystemConstUtil.cashHandleFeeRatio = cashHandleFeeRatio;
    }

    @Value("${userDefaultIntro}")
    public void setUserDefaultIntro(String userDefaultIntro) {
        SystemConstUtil.userDefaultIntro = userDefaultIntro;
    }

    @Value("${userDefaultHeadUrl}")
    public void setUserDefaultHeadUrl(String userDefaultHeadUrl) {
        SystemConstUtil.userDefaultHeadUrl = userDefaultHeadUrl;
    }

    @Value("${fileRootPath}")
    public void setFileRootPath(String fileRootPath) {
        SystemConstUtil.fileRootPath = fileRootPath;
    }

    @Value("${fileLogicRoot}")
    public void setFileLogicRoot(String fileLogicRoot) {
        SystemConstUtil.fileLogicRoot = fileLogicRoot;
    }

    @Value("${productionState}")
    public void setProductionState(boolean productionState) {
        SystemConstUtil.productionState = productionState;
    }

    @Value("${checkMch}")
    public void setCheckMch(boolean checkMch) {
        SystemConstUtil.checkMch = checkMch;
    }

    @Value("${checkMch}")
    public void setCheckSign(boolean checkSign) {
        SystemConstUtil.checkSign = checkSign;
    }

    @Value("${checkTime}")
    public void setCheckTime(boolean checkTime) {
        SystemConstUtil.checkTime = checkTime;
    }

    @Value("${reMomentFeeRatio}")
    public void setReMomentFeeRatio(BigDecimal reMomentFeeRatio) {
        if (reMomentFeeRatio.compareTo(BigDecimal.ZERO) < 0) {
            reMomentFeeRatio = BigDecimal.ZERO;
        }
        SystemConstUtil.reMomentFeeRatio = reMomentFeeRatio;
    }

    @Value("${oneUserShakeReDrawTime}")
    public void setOneUserShakeReDrawTime(Integer oneUserShakeReDrawTime) {
        SystemConstUtil.oneUserShakeReDrawTime = oneUserShakeReDrawTime;
    }
}
