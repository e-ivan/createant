package com.qnyy.re.controller;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码控制器
 * Created by E_Iva on 2017/11/24.
 */
@Controller
@RequestMapping("verify")
public class VerifyController extends BaseController{
    /**
     * 发送验证码
     */
    @RequestMapping(value = "createVerifyCode",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("发送验证码")
    public Response createVerifyCode(String phone,boolean register) {
        if (register && loginInfoService.getByPhone(phone) != null) {
            throw new BusinessException(CommonErrorResultEnum.USER_EXIST);
        }
        String verifyCode = verifyService.createVerifyCode(phone);
        return new Response(verifyCode);
    }

    /**
     * 检查手机是否注册
     */
    @RequestMapping(value = "checkRegister",method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("检查手机是否注册")
    public Response checkRegister(String phone) {
        LoginInfo loginInfo = loginInfoService.getByPhone(phone);
        Map<String,Boolean> map = new HashMap<>();
        map.put("register",loginInfo != null);
        String verifyCode = verifyService.createVerifyCode(phone);
        System.out.println(verifyCode);
        return new ObjectResponse<>(map,verifyCode);
    }
}
