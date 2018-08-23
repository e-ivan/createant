package com.qnyy.re.base.util;

import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.AddressComponentVO;
import com.qnyy.re.base.vo.UserLoginLogVO;
import com.qnyy.re.base.vo.param.*;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * 日志工具
 * Created by E_Iva on 2017.12.21.0021.
 */
public class LogUtil {
    @Autowired
    private MongoTemplate template;

    public void saveLoginLogSuccess(JoinPoint point, UserToken token) {
        try {
            UserLoginLogVO loginLog = setLoginParams(point);
            loginLog.setState(UserLoginLogVO.STATE_SUCCESS);
            if (UserLoginLogVO.TYPE_REGISTER.equals(loginLog.getType())) {
                loginLog.setMsg(loginLog.getMsg() + "注册成功");
            } else {
                loginLog.setMsg("登录成功");
            }
            loginLog.setUid(token.getUid());
            template.insert(loginLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLoginLogFault(JoinPoint point, BusinessException ex) {
        try {
            UserLoginLogVO loginLog = setLoginParams(point);
            loginLog.setState(UserLoginLogVO.STATE_FAULT);
            loginLog.setMsg(ex.getExMessage());
            template.insert(loginLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static UserLoginLogVO setLoginParams(JoinPoint point) {
        UserLoginLogVO loginLog = new UserLoginLogVO();
        loginLog.setLoginTime(new Date());
        for (Object o : point.getArgs()) {
            Class<?> clz = o.getClass();
            if (clz.getSuperclass().equals(AbstractLoginVO.class)) {
                AbstractLoginVO vo = (AbstractLoginVO) o;
                loginLog.setLat(vo.getLat());
                loginLog.setLng(vo.getLng());
                loginLog.setIp(vo.getIp());
                if (clz.equals(PhoneLoginVO.class)) {
                    System.out.println("手机登录");
                    loginLog.setLoginPhone(((PhoneLoginVO)vo).getPhone());
                    loginLog.setType(UserLoginLogVO.TYPE_PHONE);
                } else if (clz.equals(PartnerLoginVO.class)) {
                    System.out.println("第三方登录");
                    loginLog.setType(UserLoginLogVO.TYPE_WECAHT);
                    loginLog.setLoginOpenId(((PartnerLoginVO)vo).getOpenId());
                } else if (clz.equals(PartnerRegisterVO.class)) {
                    System.out.println("第三方注册");
                    loginLog.setType(UserLoginLogVO.TYPE_REGISTER);
                    loginLog.setLoginOpenId(((PartnerRegisterVO)vo).getOpenId());
                    loginLog.setLoginPhone(((PartnerRegisterVO)vo).getPhone());
                    loginLog.setMsg("微信");
                } else if (clz.equals(PhoneRegisterVO.class)) {
                    System.out.println("手机注册");
                    loginLog.setType(UserLoginLogVO.TYPE_REGISTER);
                    loginLog.setLoginPhone(((PhoneRegisterVO)vo).getPhone());
                    loginLog.setMsg("手机");
                } else if (clz.equals(CreateantLoginVO.class)) {
                    System.out.println("创蚁登录");
                    loginLog.setType(UserLoginLogVO.TYPE_CREATEANT);
                    loginLog.setLoginPhone(((CreateantLoginVO)vo).getPhone());
                    loginLog.setMsg("创蚁");
                }
            }
        }
        AddressComponentVO address = AmapUtil.getAddressByLocation(loginLog.getLng(), loginLog.getLat());
        if (address == null) {
            address = AmapUtil.getAddressByIp(loginLog.getIp());
        }
        if (address != null) {
            loginLog.setAddress(
                    StringUtils.defaultIfBlank(address.getCountry(), "中国") + "," +
                            address.getProvince() + "," +
                            address.getCity() + "," +
                            address.getDistrict() + "," +
                            address.getTownship()
            );
        }
        return loginLog;
    }

}
