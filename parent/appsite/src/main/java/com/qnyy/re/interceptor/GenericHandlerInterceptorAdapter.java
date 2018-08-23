package com.qnyy.re.interceptor;

import com.alibaba.fastjson.JSON;
import com.qnyy.re.base.entity.ApiInfo;
import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.MchInfo;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.enums.ApiInfoSite;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.enums.MchInfoState;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.service.IApiInfoService;
import com.qnyy.re.base.service.IMchService;
import com.qnyy.re.base.service.IUserTokenService;
import com.qnyy.re.base.util.DateUtil;
import com.qnyy.re.base.util.NumberUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

import static com.qnyy.re.base.enums.CommonErrorResultEnum.*;
import static com.qnyy.re.base.util.SystemConstUtil.*;

@Slf4j
public class GenericHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    @Autowired
    private IMchService mchService;
    @Autowired
    private IApiInfoService apiInfoService;
    @Autowired
    private IUserTokenService userTokenService;
    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,token");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        //获取当前访问方法
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            UnRequiredLogin unRequiredLogin = method.getMethodAnnotation(UnRequiredLogin.class);
            if (unRequiredLogin != null && !unRequiredLogin.checkSign()) {//不需要登录和验证签名接口，直接放行
                return true;
            }
            //获取请求uri
            ApiInfo apiInfo = apiInfoService.getByUri(request.getRequestURI(), ApiInfoSite.APP.getValue());
            if (apiInfo == null) {
                return true;
            }
            //获取请求头中的信息
            String mchId = request.getHeader("mchId");//商户id
            String nonceStr = request.getHeader("nonceStr");//随机数
            String timestamp = request.getHeader("timestamp");
            String signature = request.getHeader(SignUtil.SIGN_KEY_NAME);//签名
            String token = request.getHeader("token");
            String os = request.getHeader("os");
            String appVersion = request.getHeader("appVersion");
            //判断参数是否完整
            Response r;
            if (checkSign && StringUtils.isAnyBlank(checkMch ? mchId : "mchId", nonceStr, timestamp, signature)) {
                String blankName = "";
                if (checkMch && StringUtils.isBlank(mchId)) {
                    blankName = "mchId";
                } else if (StringUtils.isBlank(nonceStr)) {
                    blankName = "nonceStr";
                } else if (StringUtils.isBlank(timestamp)) {
                    blankName = "timestamp";
                } else if (StringUtils.isBlank(signature)) {
                    blankName = "signature";
                }
                r = new Response(new BusinessException(REQUEST_PARAM_LACK, blankName));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            //需要登录验证的接口，必须存在token
            if (unRequiredLogin == null && StringUtils.isBlank(token)) {
                r = new Response(new BusinessException(REQUEST_PARAM_LACK, "token"));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            //获取商户信息
            MchInfo mchInfo = mchService.getByMchId(mchId);
            if (checkMch && mchInfo == null) {
                r = new Response(new BusinessException(MCH_UN_EXIST));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            //将参数封装到IdentityHashMap（键可重复）
            IdentityHashMap<String, String> map = installRequestParamMap(request);
            map.put("mchId", mchId);
            map.put("nonceStr", nonceStr);
            map.put("timestamp", timestamp);
            map.put("token", token);
            map.put("os", os);
            map.put("appVersion", appVersion);
            map.put(SignUtil.SIGN_KEY_NAME, signature);
//            redisTemplate.opsForValue().set(nonceStr,map);
            //检查签名是否正确
            if (checkSign && !SignUtil.checkSign(map, mchInfo != null ? mchInfo.getMchKey() : "")) {
                r = new Response(new BusinessException(REQUEST_SIGN_ERROR));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            //验证请求时间
            if (checkSign && checkTime) {
                if (!NumberUtil.isNumeric(timestamp)) {
                    timestamp = "10000";
                }
                Long second = DateUtil.intervalTime(new Date(Long.parseLong(timestamp)), new Date());
                if (second > 60) {//请求超时
                    r = new Response(new BusinessException(REQUEST_TIMEOUT));
                    response.getWriter().print(JSON.toJSON(r));
                    return false;
                }
            }
            //查看商户是否拥有该接口权限
            if (checkMch) {
                String[] permissions = StringUtils.split(mchInfo.getApiPermissions(), "|");
                if (MchInfoState.CLOSE.getCode() == mchInfo.getState() || //商户关闭
                        permissions == null || permissions.length <= 0 || //商户没有权限
                        !Arrays.asList(permissions).contains(apiInfo.getId().toString())) {
                    r = new Response(new BusinessException(MCH_NO_PERMISSION));
                    response.getWriter().print(JSON.toJSON(r));
                    return false;
                }
                //设置当前商户到请求中
                request.setAttribute(UserContext.MCH_INFO, mchInfo);
            }
            //根据不同的商户获取token对应的用户
            Long userId = null;
            //系统主mch
            CommonErrorResultEnum errorResult;
            if (!checkMch || mchInfo.getTokenSource().equals(0)) {
                UserToken userToken = userTokenService.getByToken(token);
                if (userToken != null) {
                    userId = userToken.getUid();
                }
                errorResult = LOGIN_EXPIRED;
            } else {//获取商户token对应的用户信息
                LoginInfo loginInfo = loginInfoMapper.queryUserBySource(token, mchInfo.getTokenSource());
                if (loginInfo != null) {
                    userId = loginInfo.getUid();
                }
                errorResult = USER_UN_EXIST;
            }
            if (unRequiredLogin == null && userId == null) {
                r = new Response(new BusinessException(errorResult));
                response.getWriter().print(JSON.toJSON(r));
                return false;
            }
            request.setAttribute(UserContext.USER_ID, userId);
        }
        return true;
    }

    //处理参数map
    private IdentityHashMap<String, String> installRequestParamMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        IdentityHashMap<String, String> map = new IdentityHashMap<>();
        parameterMap.forEach((key, values) -> Arrays.stream(values).forEach(value -> map.put(new String(key), value)));
        return map;
    }
}