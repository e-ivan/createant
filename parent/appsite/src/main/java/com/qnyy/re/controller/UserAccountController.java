package com.qnyy.re.controller;


import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.business.query.UserAccountStatementQueryObject;
import com.qnyy.re.business.vo.param.SaveCashAccountVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/14.
 */
@Controller
@RequestMapping(value = "account")
public class UserAccountController extends BaseController {

    /**
     * 查看账户
     */
    @RequestMapping(value = "getAccount",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("查看账户")
    public Response getAccount() {
        return new ObjectResponse<>(userAccountService.getUserAccountByUser(UserContext.getUserId()));
    }

    /**
     * 用户账户流水
     */
    @RequestMapping(value = "queryAccountStatement",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("用户账户流水")
    public Response queryAccountStatement(UserAccountStatementQueryObject qo) {
        qo.setUid(UserContext.getUserId());
        qo.setOrderBy("created DESC");
        return new ObjectResponse<>(userAccountService.queryUserAccountStatement(qo));
    }

    /**
     * 创建充值订单
     */
    @RequestMapping(value = "createRechargeOrder", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("创建充值订单")
    public Response createRechargeOrder(BigDecimal amount) {
        return new ObjectResponse<>(rechargeOrderService.createRechargeOrder(UserContext.getUserId(), amount));
    }

    /**
     * 绑定提现账户
     */
    @RequestMapping(value = "saveCashAccount", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("绑定提现账户")
    public Response bindCashAccount(SaveCashAccountVO vo) {
        userCashAccountService.save(vo);
        return new Response("已保存");
    }

    @RequestMapping(value = "getCashAccount",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @ApiDocument("获取提现账户")
    public Response getCashAccount() {
        return new ObjectResponse<>(userCashAccountService.getCashAccountByUser(UserContext.getUserId()));
    }

    /**
     * 提现申请
     */
    @RequestMapping(value = "addCash", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("提现申请")
    public Response addCash(BigDecimal amount) {
        cashService.addCash(amount);
        return new Response("您的提现已提交！");
    }
}
