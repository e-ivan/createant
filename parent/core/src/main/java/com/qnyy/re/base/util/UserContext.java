package com.qnyy.re.base.util;

import com.qnyy.re.base.entity.MchInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class UserContext {
    public static final String MCH_INFO = "MCH_INFO";
    public static final String USER_ID = "USER_ID";

    /**
     * 获取用户id
     * @return
     */
    public static Long getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Long) request.getAttribute(USER_ID);
    }

    /**
     * 获取商户信息
     */
    public static MchInfo getMchInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (MchInfo)request.getAttribute(MCH_INFO);
    }



}
