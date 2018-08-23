package com.qnyy.re.interceptor;

import com.alibaba.fastjson.JSON;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserTokenService;
import com.qnyy.re.base.util.DateUtil;
import com.qnyy.re.base.util.NumberUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.util.SHA1Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by dly on 2016/11/25.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private IUserTokenService tokenService;
    private static boolean useSign = true;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,token");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            UnRequiredLogin unRequiredLogin = hm.getMethodAnnotation(UnRequiredLogin.class);
            //查看是否需要登录验证
            if (unRequiredLogin != null && !unRequiredLogin.checkSign()) {//不检查签名
                //放行
                return true;
            }
            //将参数封装到IdentityHashMap（键可重复）
            IdentityHashMap<String, String> map = installRequestParamMap(request);
            Response r;
            String sign = request.getParameter(SHA1Util.SIGN_KEY_NAME);
            //获取请求时间
            if (useSign) {
                String timestamp = request.getParameter("timestamp");
                if (StringUtils.isBlank(timestamp) || !NumberUtil.isNumeric(timestamp) || StringUtils.isBlank(sign)) {
                    r = new Response(new BusinessException(CommonErrorResultEnum.REQUEST_SIGN_ERROR));
                    response.getWriter().print(JSON.toJSON(r));
                    return false;
                }
                Integer minute = DateUtil.intervalTime(new Date(Long.parseLong(timestamp)), new Date(), DateUtil.MINUTE);
                if (minute > 5) {//请求超时
                    r = new Response(new BusinessException(CommonErrorResultEnum.REQUEST_TIMEOUT));
                    response.getWriter().print(JSON.toJSON(r));
                    return false;
                }
            }
            String uid = request.getHeader("userId");
            String token = request.getHeader("token");
            request.setAttribute(UserContext.USER_ID,StringUtils.isBlank(uid) ? null : Long.parseLong(uid));
            if (unRequiredLogin != null) {
                return signParams(response, map, token, sign);
            }
            UserToken userToken = tokenService.getTokenByUid(uid);
            if (userToken == null || !userToken.getToken().equals(token)) {
                r = new Response(new BusinessException(CommonErrorResultEnum.LOGIN_EXPIRED));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            return signParams(response, map, token, sign);
        }
        return true;
    }

    private static boolean signParams(HttpServletResponse response, Map<String, String> map, String token, String sign) throws IOException {
        if (useSign) {
            //查看请求参数签名是否正确
            if (SHA1Util.compareTokenkey(map, token, sign)) {
                return true;
            }
            response.getWriter().print(JSON.toJSON(new Response(CommonErrorResultEnum.REQUEST_SIGN_ERROR)));
            return false;
        } else {
            return true;
        }
    }

    //处理参数map
    private IdentityHashMap<String, String> installRequestParamMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        //将参数封装到IdentityHashMap（键可重复）
        IdentityHashMap<String, String> map = new IdentityHashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 1) {
                map.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
            } else {
                map.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return map;
    }

}
