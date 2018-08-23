package com.qnyy.re.controller;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.*;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dly on 2016/8/12.
 */
@Slf4j
public abstract class BaseController {
    @Autowired
    protected IFileService fileService;
    @Autowired
    protected ILoginInfoService loginInfoService;
    @Autowired
    protected IUserInfoService userInfoService;
    @Autowired
    protected IUserTokenService userTokenService;
    @Autowired
    protected IVerifyService verifyService;
    @Autowired
    protected IReMomentService reMomentService;
    @Autowired
    protected IPayUtilService payUtilService;
    @Autowired
    protected IBaseOrderService baseOrderService;
    @Autowired
    protected IUserMsgLogService userMsgLogService;
    @Autowired
    protected IUserAccountService userAccountService;
    @Autowired
    protected ICashService cashService;
    @Autowired
    protected IUserCashAccountService userCashAccountService;
    @Autowired
    protected IRechargeOrderService rechargeOrderService;
    @Autowired
    protected IUserClassRelationService userClassRelationService;
    @Autowired
    protected IUserFeedbackService userFeedbackService;
    @Autowired
    protected IInformantCenterService informantCenterService;
    @Autowired
    protected ISysBroadcastService sysBroadcastService;
    @Autowired
    protected IAppVersionService appVersionService;
    @Autowired
    protected IWeiXinMpService weiXinMpService;

    /**
     * 获取请求地址
     * @param request
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @ExceptionHandler
    public @ResponseBody
    Response runtimeExceptionHandler(Exception ex) {
        // 只过滤定义异常
        if(ex instanceof BusinessException) {
            return new Response((BusinessException)ex);
        }
        ex.printStackTrace();
        log.error(ex.toString());
        return new Response(new BusinessException(CommonErrorResultEnum.LOGIC_ERROR,ex.toString().replaceAll("\r", "<br/>")));
    }

}
