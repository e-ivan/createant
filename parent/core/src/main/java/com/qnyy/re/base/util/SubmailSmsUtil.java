package com.qnyy.re.base.util;

import com.submail.sdk.MESSAGEXsend;
import com.submail.sdk.config.AppConfig;
import com.submail.sdk.utils.ConfigLoader;
import com.qnyy.re.base.vo.VerifyCodeVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信工具
 * Created by E_Iva on 2018.1.5.0005.
 */
@Component
public class SubmailSmsUtil {

    private static final String VERIFY_CODE = "code";

    private static final String VALID_TIME = "time";

    private static String verifyCodeProjectId;

    private static AppConfig config;

    static {
        config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
    }

    @Value("${verifyCodeProjectId}")
    public void setVerifyCodeProjectId(String verifyCodeProjectId) {
        SubmailSmsUtil.verifyCodeProjectId = verifyCodeProjectId;
    }

    public static boolean sendVerifyCode(String code, String phone) {
        MESSAGEXsend submail = new MESSAGEXsend(config);
        submail.setProject(verifyCodeProjectId);
        submail.addVar(VERIFY_CODE, code);
        submail.addVar(VALID_TIME, VerifyCodeVO.VALID_MINUTE.toString());
        submail.addTo(phone);
        return submail.xsend();
    }
}
