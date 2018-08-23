package com.qnyy.re.controller;

import com.alibaba.fastjson.JSONObject;
import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.ObjectResponse;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.container.Response;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.ReItem;
import com.qnyy.re.business.entity.ReMoment;
import com.qnyy.re.business.entity.ShakearoundDevice;
import com.qnyy.re.business.enums.*;
import com.qnyy.re.business.mapper.ReItemMapper;
import com.qnyy.re.business.query.ReCommentQueryObject;
import com.qnyy.re.business.query.ReItemQueryObject;
import com.qnyy.re.business.query.ReMomentQueryObject;
import com.qnyy.re.business.util.AdvertisingUtil;
import com.qnyy.re.business.util.CreateantRequestUtil;
import com.qnyy.re.business.util.WeiXinMpRequestUtil;
import com.qnyy.re.business.vo.RedEnvelopeVO;
import com.qnyy.re.business.vo.param.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 红包控制器
 * Created by E_Iva on 2017.11.29 0029.
 */
@Controller
@RequestMapping("reMoment")
public class ReMomentController extends BaseController {
    /**
     * 创建红包
     *
     * @param vo
     */
    @RequestMapping(value = "createReMoment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("创建红包")
    public Response createReMoment(CreateReMomentVO vo) {
        return new ObjectResponse<>(reMomentService.createReMoment(vo));
    }

    /**
     * 创建个人红包
     */
    @RequestMapping(value = "createSingleRe", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("创建个人红包")
    public Response createSingleRe(CreateSingleReVO vo) {
        return new ObjectResponse<>(reMomentService.createSingleRe(vo));
    }

    /**
     * 获取单人红包
     */
    @RequestMapping(value = "getSingleRe", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("获取单人红包")
    public Response getSingleRe(Long reId) {
        return new ObjectResponse<>(reMomentService.getSingleRe(reId));
    }

    /**
     * 查询两个用户是否有领取敲门红包
     */
    @RequestMapping(value = "checkSingleReDraw", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("查询两个用户是否有领取敲门红包")
    public Response checkSingleReDraw(Long uid) {
        return new ObjectResponse<>(reMomentService.checkSingleReDraw(UserContext.getUserId(), uid));
    }

    @Resource
    private ReItemMapper reItemMapper;

    private void disposeReMomentDrawState(Collection<RedEnvelopeVO> rms) {
        List<Long> ids = rms.stream().map(RedEnvelopeVO::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            ReItemQueryObject itemQo = new ReItemQueryObject(null, ids, UserContext.getUserId(), ReItemStateEnum.DRAW.getCode(), ReMomentTypeEnum.MOMENT.getCode(), null,null);
            itemQo.setPageSize(PageResult.MAX_SIZE);
            List<ReItem> items = reItemMapper.query(itemQo);
            if (CollectionUtils.isNotEmpty(items)) {
                List<Long> drawRmIds = items.stream().map(ReItem::getReId).collect(Collectors.toList());
                rms.forEach(r -> {
                    if (drawRmIds.contains(r.getId())) {
                        r.setDrawState(ReMomentDrawStateEnum.DRAW.getCode());
                    }
                });
            }
        }
    }

    /**
     * 查询附近的红包
     */
    @RequestMapping(value = "queryNearbyRedEnvelope", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("查询附近的红包")
    public Response queryNearbyRedEnvelope(QueryNearbyRedEnvelopeVO vo) {
        vo.setUid(UserContext.getUserId());
        List<RedEnvelopeVO> list = reMomentService.queryNearRe(vo);
        //随机添加广告红包
//        list.addAll(Stream.generate(AdvertisingUtil::getRedEnvelope).limit(new Random().nextInt(5)).collect(Collectors.toList()));
        return new ObjectResponse<>(list);
    }

    /**
     * 获取门店红包 TODO 和获取摇一摇红包接口功能重复
     */
    @RequestMapping(value = "getStoreRe", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("获取门店红包")
    public Response getStoreRe(Long storeId) {
        if (storeId == null) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "storeId");
        }
        ReMomentQueryObject qo = new ReMomentQueryObject();
        qo.setStoreId(storeId);
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setCrowds(new int[]{ReMomentCrowdEnum.SHAKE.getCode()});
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode()});
// 如果用户可以重复领取门店的红包，则使用此参数，并需要修改领取红包中摇一摇红包的逻辑
//        qo.setRemoveUid(UserContext.getUserId());
        qo.setPageSize(1);
        qo.setOrderBy("m.created");
        List<RedEnvelopeVO> rms = reMomentService.queryReMoment(qo).getResult();
        this.disposeReMomentDrawState(rms);
        return new ObjectResponse<>(rms);
    }

    /**
     * 获取摇一摇红包
     */
    @RequestMapping(value = "getShakeRe", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("获取摇一摇红包")
    public Response getShakeRe(GetShakeReVO vo) {
        ReMomentQueryObject qo = new ReMomentQueryObject();
        qo.setStoreId(vo.getStoreId());
        //获取创客红包
        if (vo.getUid() != null) {
            qo.setUid(vo.getUid());
            qo.setStoreId(0L);
        }
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setCrowds(new int[]{ReMomentCrowdEnum.SHAKE.getCode()});
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode()});
// 如果用户可以重复领取门店的红包，则使用此参数，并需要修改领取红包中摇一摇红包的逻辑
//        qo.setRemoveUid(UserContext.getUserId());
        qo.setPageSize(1);
        qo.setOrderBy("m.created");
        List<RedEnvelopeVO> rms = reMomentService.queryReMoment(qo).getResult();
        this.disposeReMomentDrawState(rms);
        return new ObjectResponse<>(rms);
    }

    /**
     * 领取红包
     */
    @RequestMapping(value = "drawRedEnvelope", method = RequestMethod.POST)
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("领取红包")
    public Response drawRedEnvelope(DrawRedEnvelopeVO vo) {
        if (vo.getReId() != null) {
            vo.setUid(UserContext.getUserId());
        } else {
            //查询正在要的设备
            JSONObject shakeInfo = WeiXinMpRequestUtil.getShakeInfo(vo.getTicket());
            ShakearoundDevice beaconInfo = shakeInfo.getObject("beacon_info", ShakearoundDevice.class);
            //查询设备所拥有的红包：设备-->门店-->红包
            Long reId = weiXinMpService.getDeviceReMomentId(beaconInfo.getMajor(), beaconInfo.getMinor());
            if (reId == null) {
                throw new BusinessException(CommonErrorResultEnum.RE_STORE_NO_DRAW);
            }
            vo.setReId(reId);
            //获取到用户信息
            String openid = shakeInfo.getString("openid");
            String createantToken = CreateantRequestUtil.getCreateantTokenByMpOpenId(openid);
            LoginInfo loginInfo = loginInfoService.getBySource(createantToken, RegisterSourceEnum.CREATEANT.getCode());
            vo.setUid(loginInfo.getUid());
        }
        ReMoment reMoment = reMomentService.drawRedEnvelope(vo);
        return new ObjectResponse<>(reMoment);
    }

    /**
     * 压测领取红包
     */
    @RequestMapping(value = "drawRedEnvelopeTest", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    @UnRequiredLogin
    @ApiDocument("压测领取红包")
    public Response drawRedEnvelopeTest(DrawRedEnvelopeVO vo) {
        if (StringUtils.isBlank(vo.getTicket())) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "ticket");
        }
        //查询正在要的设备
        WeiXinMpRequestUtil.getAccessToken();
        //查询设备所拥有的红包：设备-->门店-->红包
        try {
            CreateantRequestUtil.getCreateantTokenByMpOpenId("");
        } catch (Exception ignored) {
        }
        LoginInfo loginInfo = loginInfoService.getBySource(vo.getTicket(), RegisterSourceEnum.CREATEANT.getCode());
        vo.setUid(loginInfo.getUid());
        ReMoment reMoment = reMomentService.drawRedEnvelope(vo);
        return new ObjectResponse<>(reMoment);
    }

    /**
     * 查看红包详情
     */
    @RequestMapping(value = "viewRedEnvelope", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("查看红包详情")
    public Response viewRedEnvelope(Long reId) {
        return new ObjectResponse<>(reMomentService.viewReMoment(reId));
    }

    /**
     * 获取红包领取明细
     */
    @RequestMapping(value = "queryReDrawItem", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("获取红包领取明细")
    public Response queryReDrawItem(ReItemQueryObject qo) {
        qo.setState(ReItemStateEnum.DRAW.getCode());
        qo.setOrderBy(" r.draw_time");
        return new ObjectResponse<>(reMomentService.queryReDrawItem(qo));
    }

    /**
     * 获取用户领取红包明细
     */
    @RequestMapping(value = "queryDrawReMoment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("获取用户领取红包明细")
    public Response queryDrawReMoment(ReMomentQueryObject qo) {
        qo.setUid(UserContext.getUserId());
        qo.setTypes(new int[]{ReMomentTypeEnum.MOMENT.getCode()});
        qo.setQueryType(ReMomentQueryObject.QUERY_DRAW_RE);
        qo.setStates(new int[]{ReItemStateEnum.DRAW.getCode()});
        qo.setOrderBy("i.draw_time DESC");
        PageResult<RedEnvelopeVO> page = reMomentService.queryReMoment(qo);
        //todo 获取广告商
        UserInfo user = AdvertisingUtil.getAdUserInfo();
        //封装广告商信息
        page.getResult().stream().filter(r -> r.getUser() == null).forEach(r -> r.setUser(user));
        return new ObjectResponse<>(page);
    }

    /**
     * 用户发送红包记录
     */
    @RequestMapping(value = "queryPayReMoment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("用户发送红包记录")
    public Response queryPayReMoment(ReMomentQueryObject qo) {
        qo.setUid(UserContext.getUserId());
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setTypes(new int[]{ReMomentTypeEnum.MOMENT.getCode()});
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode(), ReMomentStateEnum.FINISH.getCode()});
        qo.setOrderBy("m.state,m.created DESC");
        return new ObjectResponse<>(reMomentService.queryReMoment(qo));
    }

    /**
     * 获取用户生活圈
     */
    @RequestMapping(value = "queryUserMoment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("获取用户生活圈")
    public Response queryUserMoment(ReMomentQueryObject qo) {
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setTypes(new int[]{ReMomentTypeEnum.MOMENT.getCode()});
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode(), ReMomentStateEnum.FINISH.getCode()});
        qo.setOrderBy("m.created DESC");
        return new ObjectResponse<>(reMomentService.queryReMoment(qo));
    }

    /**
     * 发现有趣的事
     */
    @RequestMapping(value = "discoverMatter", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("发现有趣的事")
    public Response discoverMatter(ReMomentQueryObject qo) {
        qo.setUid(-1L);
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setTypes(new int[]{ReMomentTypeEnum.MOMENT.getCode()});
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode(), ReMomentStateEnum.FINISH.getCode()});
        qo.setCrowds(new int[]{ReMomentCrowdEnum.MALE.getCode(), ReMomentCrowdEnum.JUNIOR.getCode(),
                ReMomentCrowdEnum.FEMALE.getCode(), ReMomentCrowdEnum.CREATEANT.getCode(), ReMomentCrowdEnum.ALL.getCode()});
        qo.setOrderBy("m.created DESC");
        return new ObjectResponse<>(reMomentService.queryReMoment(qo));
    }


    /**
     * 添加评论
     */
    @RequestMapping(value = "addComment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("添加评论")
    public Response addComment(AddReCommentVO vo) {
        reMomentService.addComment(vo);
        return new Response("评论成功");
    }

    /**
     * 查询评论
     * 只传reId获取指定红包一级评论列表
     * 传parentId获取二级评论列表
     */
    @RequestMapping(value = "queryComment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("查询评论")
    public Response queryComment(ReCommentQueryObject qo) {
        qo.setState(ReCommentStateEnum.NORMAL.getCode());
        return new ObjectResponse<>(reMomentService.queryReComment(qo));
    }

    /**
     * 获取指定评论
     */
    @RequestMapping(value = "getComment", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("获取指定评论")
    public Response getComment(Long rcId) {
        return new ObjectResponse<>(reMomentService.getReComment(rcId));
    }

    /**
     * 红包点赞
     */
    @RequestMapping(value = "handleUserVote", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument("红包点赞")
    public Response handleUserVote(Long reId, boolean vote) {
        reMomentService.handleUserVote(reId, vote);
        return new Response(vote ? "已点赞" : "取消点赞");
    }

}
