package com.qnyy.re.controller;

import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.enums.FilePurposeEnum;
import com.qnyy.re.base.enums.FileTypeEnum;
import com.qnyy.re.base.query.UserFansQueryObject;
import com.qnyy.re.base.query.UserStatisticsQueryObject;
import com.qnyy.re.base.query.UserVisitHistoryQueryObject;
import com.qnyy.re.base.util.RongYunUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.vo.param.*;
import com.qnyy.re.business.enums.RegisterSourceEnum;
import com.qnyy.re.business.query.UserClassRelationQueryObject;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录控制器
 * Created by E_Iva on 2017/11/24.
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    /**
     * 合作注册接口
     */
    @RequestMapping(value = "partnerRegister", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("合作注册接口")
    public Response partnerRegister(HttpServletRequest request,PartnerRegisterVO vo) {
        vo.setIp(getIpAddr(request));
        verifyService.checkVerifyCode(vo.getPhone(),vo.getCode());
        UserToken userToken = loginInfoService.partnerRegister(vo);
        verifyService.updateVerifyCodeUse(vo.getPhone(),vo.getCode());
        return new ObjectResponse<>(userToken,"注册成功");
    }

    /**
     * 合作登录接口
     */
    @RequestMapping(value = "partnerLogin", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("合作登录接口")
    public Response partnerLogin(HttpServletRequest request,PartnerLoginVO vo) {
        vo.setIp(getIpAddr(request));
        return new ObjectResponse<>(loginInfoService.partnerLogin(vo),"登录成功");
    }

    /**
     * 创蚁平台专用登录接口
     */
    @RequestMapping(value = "createantLogin", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("创蚁登录接口")
    public Response createantLogin(HttpServletRequest request,CreateantLoginVO vo) {
        vo.setIp(getIpAddr(request));
        vo.setSource(RegisterSourceEnum.CREATEANT.getCode());
        return new ObjectResponse<>(loginInfoService.createantLogin(vo),"登录成功");
    }



    /**
     * 手机注册
     */
    @RequestMapping(value = "phoneRegister", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("手机注册")
    public Response phoneRegister(HttpServletRequest request,PhoneRegisterVO vo) {
        vo.setIp(getIpAddr(request));
        verifyService.checkVerifyCode(vo.getPhone(),vo.getCode());
        UserToken userToken = loginInfoService.phoneRegister(vo);
        verifyService.updateVerifyCodeUse(vo.getPhone(),vo.getCode());
        return new ObjectResponse<>(userToken,"注册成功");
    }


    /**
     * 手机登录
     */
    @RequestMapping(value = "phoneLogin", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("手机登录")
    public Response phoneLogin(HttpServletRequest request,PhoneLoginVO vo) {
        vo.setIp(getIpAddr(request));
        return new ObjectResponse<>(loginInfoService.phoneLogin(vo),"登录成功");
    }

    /**
     * 通过手机修改密码
     */
    @RequestMapping(value = "resetPasswordByPhone", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("通过手机修改密码")
    public Response resetPasswordByPhone(HttpServletRequest request,ResetPasswordByPhoneVO vo) {
        vo.setIp(getIpAddr(request));
        verifyService.checkVerifyCode(vo.getPhone(),vo.getCode());
        loginInfoService.resetPasswordByPhone(vo);
        verifyService.updateVerifyCodeUse(vo.getPhone(),vo.getCode());
        return new Response("修改成功，请重新登录");
    }

    /**
     * 通过就密码修改密码
     */
    @RequestMapping(value = "resetPasswordByOld", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("通过就密码修改密码")
    public Response resetPasswordByOld(HttpServletRequest request,ResetPasswordByOldVO vo) {
        vo.setIp(getIpAddr(request));
        loginInfoService.resetPasswordByOld(vo);
        return new Response("修改成功，请重新登录");
    }


    /**
     * 更新位置信息
     * @param lng 经度
     * @param lat 纬度
     */
    @RequestMapping(value = "updateLocation", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("更新位置信息")
    public Response updateLocation(String lng, String lat) {
        userInfoService.updateLocation(UserContext.getUserId(), lng, lat);
        return new Response("更新成功");
    }

    /**
     * 绑定手机号码
     */
    @RequestMapping(value = "bindUserPhone", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("绑定手机号码")
    public Response bindUserPhone(String phone, String code) {
        verifyService.checkVerifyCode(phone, code);
        loginInfoService.bindUserPhone(phone, UserContext.getUserId());
        verifyService.updateVerifyCodeUse(phone, code);
        return new Response("手机绑定成功");
    }

    /**
     * 绑定第三方账户
     */
    @RequestMapping(value = "bindPartnerOpenId", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("绑定第三方账户")
    public Response bindPartnerOpenId(String openId,Integer source) {
        loginInfoService.bindPartnerOpenId(UserContext.getUserId(),openId,source);
        return new Response("绑定成功");
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "getUserInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("获取用户信息")
    public Response getUserInfo(Long uid) {
        return new ObjectResponse<>(userInfoService.getByUid(uid == null ? UserContext.getUserId() : uid));
    }

    /**
     * 获取用户统计信息
     */
    @RequestMapping(value = "getUserStatistics",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("获取用户统计信息")
    public Response getUserStatistics() {
        return new ObjectResponse<>(userInfoService.getStatisticsByUid(UserContext.getUserId()));
    }


    /**
     * 更新用户信息
     */
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("更新用户信息")
    public Response updateUserInfo(UpdateUserInfoVO vo) throws Exception {
        if (vo.getFile() != null) {
            List<UploadFile> uploadFiles = fileService.saveFile(FileTypeEnum.IMAGE, FilePurposeEnum.USER_HEAD_IMG, UserContext.getUserId(), UserContext.getUserId(), vo.getFile());
            vo.setHeadUrl(uploadFiles.get(0).getPath());
        }
        return new ObjectResponse<>(userInfoService.updateUserInfo(vo));
    }

    /**
     * 更新融云token
     */
    @RequestMapping(value = "updateRongYunToken",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("更新融云token")
    public Response updateRongYunToken() throws Exception {
        UserInfo user = userInfoService.getByUid(UserContext.getUserId());
        String rongYunToken = RongYunUtil.registerUserToken(user.getUid(), user.getNickname(), user.getHeadUrl());
        return new ObjectResponse<>(userTokenService.updateRongYunToken(user.getUid(),rongYunToken));
    }

    /**
     * 查看用户信息
     */
    @RequestMapping(value = "viewUserInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查看用户信息")
    public Response viewUserInfo(Long uid) {
        UserVisitHistoryQueryObject qo = new UserVisitHistoryQueryObject();
        qo.setByVisitUid(uid);
        PageResult<UserInfo> result = userInfoService.queryUserVisitHistory(qo);
        UserInfo userInfo = userInfoService.getByUid(uid);
        UserStatistics statistics = userInfoService.getStatisticsByUid(uid);
        userInfoService.addUserHistory(uid);
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo",userInfo);
        map.put("statistics",statistics);
        map.put("visitor",result.getResult());
        map.put("fan",userInfoService.checkIfUserFan(uid));
        map.put("hadKnock",reMomentService.checkSingleReDraw(UserContext.getUserId(),uid));
        return new ObjectResponse<>(map);
    }

    /**
     * 获取访客记录
     * byVisitUid 获取的用户
     */
    @RequestMapping(value = "queryUserVisitor",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("获取访客记录")
    public Response queryUserVisitor(UserVisitHistoryQueryObject qo) {
        return new ObjectResponse<>(userInfoService.queryUserVisitHistory(qo));
    }

    /**
     * 关注或取消关注
     */
    @RequestMapping(value = "handleUserFans",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("关注或取消关注")
    public Response handleUserFans(Long uid,boolean add) {
        userInfoService.handleUserFans(uid, add);
        return new Response(add ? "已关注" : "取消关注");
    }

    /**
     * 查询关注对象
     */
    @RequestMapping(value = "queryMyCare",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查询关注对象")
    public Response queryMyCare(UserFansQueryObject qo) {
        qo.setFanUid(UserContext.getUserId());
        return new ObjectResponse<>(userInfoService.queryUserFans(qo));
    }

    /**
     * 查询粉丝
     */
    @RequestMapping(value = "queryMyFans",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查询粉丝")
    public Response queryMyFans(UserFansQueryObject qo) {
        qo.setByFanUid(UserContext.getUserId());
        return new ObjectResponse<>(userInfoService.queryUserFans(qo));
    }

    /**
     * 查询任性用户
     */
    @RequestMapping(value = "queryPayTopUser",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查询任性用户")
    public Response queryPayTopUser(UserStatisticsQueryObject qo) {
        qo.setPageSize(12);
        qo.setCurrentPage(1);
        qo.setMinRePayAmount(1);
        qo.setOrderBy("s.re_pay_amount DESC");
        return new ObjectResponse<>(userInfoService.queryUserByStatistics(qo));
    }

    /**
     * 查询收益明细
     */
    @RequestMapping(value = "queryReap",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查询收益明细")
    public Response queryReap(UserClassRelationQueryObject qo) {
        qo.setUid(UserContext.getUserId());
        qo.setOrderBy("created DESC");
        return new ObjectResponse<>(userClassRelationService.queryReap(qo));
    }

    /**
     * 更新创蚁用户类型
     */
    @RequestMapping(value = "updateUserType",method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("更新创蚁用户类型")
    public Response updateUserType(String type) {
        userInfoService.updateUserType(UserContext.getUserId(),type);
        return new Response();
    }



    //删除用户
    @RequestMapping(value = "delUser",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @UnRequiredLogin(checkSign = false)
    @ApiDocument("删除用户")
    public Response delUser(String phone,Long uid) {
        if (SystemConstUtil.productionState) {
            return new Response("请联系技术人员");
        }
        loginInfoService.deleteUser(phone, uid);
        return new Response("已删除");
    }
}
