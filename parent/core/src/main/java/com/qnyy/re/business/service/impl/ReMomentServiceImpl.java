package com.qnyy.re.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.enums.FilePurposeEnum;
import com.qnyy.re.base.enums.FileTypeEnum;
import com.qnyy.re.base.enums.UserMsgLogTypeEnum;
import com.qnyy.re.base.mapper.UserInfoMapper;
import com.qnyy.re.base.service.IFileService;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.service.IUserMsgLogService;
import com.qnyy.re.base.util.*;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.AddressComponentVO;
import com.qnyy.re.business.entity.*;
import com.qnyy.re.business.enums.*;
import com.qnyy.re.business.mapper.*;
import com.qnyy.re.business.query.ReCommentQueryObject;
import com.qnyy.re.business.query.ReItemQueryObject;
import com.qnyy.re.business.query.ReMomentQueryObject;
import com.qnyy.re.business.service.IBaseOrderService;
import com.qnyy.re.business.service.IReMomentService;
import com.qnyy.re.business.service.IUserClassRelationService;
import com.qnyy.re.business.service.IWeiXinMpService;
import com.qnyy.re.business.util.AdvertisingUtil;
import com.qnyy.re.business.util.OrderUtil;
import com.qnyy.re.business.util.RedEnvelopesUtil;
import com.qnyy.re.business.vo.RedEnvelopeVO;
import com.qnyy.re.business.vo.param.*;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by E_Iva on 2017.11.29 0029.
 */
@Service
@Log4j
public class ReMomentServiceImpl implements IReMomentService {

    @Resource
    private ReMomentMapper reMomentMapper;
    @Resource
    private ReOrderMapper reOrderMapper;
    @Resource
    private ReItemMapper reItemMapper;
    @Resource
    private ReCommentMapper reCommentMapper;
    @Resource
    private ReVoteUserMapper voteUserMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    private IBaseOrderService baseOrderService;
    @Autowired
    private IUserMsgLogService userMsgLogService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private IUserClassRelationService userClassRelationService;
    @Autowired
    private IWeiXinMpService weiXinMpService;
    @Autowired
    private IFileService fileService;

    @Override
    public ReOrder createReMoment(CreateReMomentVO vo) {
        UserInfo user = userInfoService.getByUid(UserContext.getUserId());
        if (vo.getReAmount().compareTo(new BigDecimal("1")) < 0) {
            throw new BusinessException(CommonErrorResultEnum.RE_AMOUNT_ERROR, "不能小于一元");
        }
        if (vo.getReNum() < 1) {
            throw new BusinessException(CommonErrorResultEnum.RE_NUM_ERROR);
        }
        //抽取费用
        BigDecimal cost = vo.getReAmount().multiply(SystemConstUtil.reMomentFeeRatio);
        RedEnvelopesUtil.checkRight(NumberUtil.fromYuan2Fen(vo.getReAmount().subtract(cost)), vo.getReNum());
        //创蚁会员的情况
//        if (ReMomentCrowdEnum.CREATEANT.getCode().equals(vo.getCrowd())) {
//            if (!user.getCreateantMember() || !user.getCreateantMerchant()) {
//                throw new BusinessException(CommonErrorResultEnum.RE_NOT_CREATANT);
//            }
//        }
        //如果是摇一摇红包
        if (ReMomentCrowdEnum.SHAKE.getCode().equals(vo.getCrowd())) {
            //查找用户店铺
            List<Long> storeIds = weiXinMpService.queryUserStoreIds(user.getUid());
            if (!storeIds.contains(vo.getStoreId())) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_NOP);
            }
        }
        ReMoment re = new ReMoment();
        re.setState(ReMomentStateEnum.WAIT.getCode());
        re.setUid(user.getUid());
        re.setContent(vo.getContent());
        re.setCrowd(vo.getCrowd());
        re.setDrawAmount(cost);
        re.setType(ReMomentTypeEnum.MOMENT.getCode());
        re.setLinkTo(vo.getLinkTo());
        re.setReAmount(vo.getReAmount());
        re.setReNum(vo.getReNum());
        re.setScope(vo.getScope());
        re.setUserLng(vo.getUserLng());
        re.setUserLat(vo.getUserLat());
        re.setReLng(vo.getReLng());
        re.setReLat(vo.getReLat());
        re.setStoreId(vo.getStoreId());
        re.setShopId(vo.getShopId());
        AddressComponentVO address = AmapUtil.getAddressByLocation(re.getReLng(), re.getReLat());
        if (address != null) {
            re.setCityId(NumberUtils.toInt(address.getCitycode()));
            re.setDistrictId(NumberUtils.toInt(address.getAdcode()));
        }
        if (CollectionUtils.isNotEmpty(vo.getFileId())) {
            List<UploadFile> files = fileService.queryFile(vo.getFileId());
            re.setPicUrl(JSON.toJSONString(files));
        }
        reMomentMapper.insert(re);
        try {
            if (vo.getFile() != null) {
                ReMoment reMoment = reMomentMapper.selectByPrimaryKey(re.getId());
                List<UploadFile> files = fileService.saveFile(FileTypeEnum.IMAGE, FilePurposeEnum.RE_MOMENT, user.getUid(), re.getId(), vo.getFile());
                reMoment.setPicUrl(JSON.toJSONString(files));
                reMomentMapper.updateByPrimaryKey(reMoment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorResultEnum.FILE_UPLOAD_ERROR);
        }
        return createReOrder(user.getUid(), re.getId(), vo.getReAmount());
    }

    private void updateReMoment(ReMoment reMoment){
        if (reMomentMapper.updateByPrimaryKey(reMoment) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    @Override
    public ReOrder createSingleRe(CreateSingleReVO vo) {
        UserInfo user = userInfoService.getByUid(UserContext.getUserId());
        if (vo.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.RE_AMOUNT_ERROR);
        }
        RedEnvelopesUtil.checkRight(NumberUtil.fromYuan2Fen(vo.getAmount()), 1);
        ReMoment re = new ReMoment();
        re.setState(ReMomentStateEnum.WAIT.getCode());
        re.setUid(user.getUid());
        re.setContent(vo.getContent());
        re.setCrowd(ReMomentCrowdEnum.ALL.getCode());
        re.setType(ReMomentTypeEnum.SINGLE.getCode());
        re.setReAmount(vo.getAmount());
        re.setReNum(1);
        reMomentMapper.insert(re);
        return createReOrder(user.getUid(), re.getId(), vo.getAmount());
    }

    @Override
    public ReOrder getByOrderId(String orderId) {
        return this.reOrderMapper.getByOrderId(orderId);
    }

    @Override
    public ReOrder setOrderSuccess(String orderId) {
        ReOrder reOrder = this.getByOrderId(orderId);
        reOrder.setState(OrderUtil.OrderState.PAY.getCode());
        reOrder.setPayTime(new Date());
        this.updateReOrder(reOrder);
        this.setReMomentOnline(reOrder.getReId());
        userClassRelationService.countReap(reOrder.getUid(), reOrder.getPayAmount());
        return reOrder;
    }

    @Override
    public void updateReOrder(BaseOrder reOrder) {
        if (reOrderMapper.updateByPrimaryKey(reOrder) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    @Override
    public List<RedEnvelopeVO> queryNearRe(QueryNearbyRedEnvelopeVO vo) {
        ReMomentQueryObject qo = new ReMomentQueryObject();
        UserInfo user = userInfoMapper.selectByPrimaryKey(vo.getUid());
        qo.setLng(vo.getLng());
        qo.setLat(vo.getLat());
        boolean isAdmin = user != null && user.admin();
        if (isAdmin) {
            qo.setLng(null);
            qo.setLat(null);
        }
        //获取经纬度地址
        AddressComponentVO address = AmapUtil.getAddressByLocation(vo.getLng(), vo.getLat());
        if (address == null) {
            throw new BusinessException(CommonErrorResultEnum.LOCATION_ERROR);
        }
        qo.setCityId(NumberUtils.toInt(address.getCitycode()));
        qo.setDistrictId(NumberUtils.toInt(address.getAdcode()));
        qo.setStates(new int[]{ReMomentStateEnum.NORMAL.getCode()});
        qo.setQueryType(ReMomentQueryObject.QUERY_PAY_RE);
        qo.setPageSize(50);
        qo.setRemoveUid(vo.getUid());
        qo.setTypes(new int[]{ReMomentTypeEnum.MOMENT.getCode(), ReMomentTypeEnum.ACTIVITY.getCode()});
        qo.setCrowds(new int[]{ReMomentCrowdEnum.MALE.getCode(), ReMomentCrowdEnum.JUNIOR.getCode(),
                ReMomentCrowdEnum.FEMALE.getCode(), ReMomentCrowdEnum.CREATEANT.getCode(), ReMomentCrowdEnum.ALL.getCode()});
        /*List<Long> reIds = reMoments.stream().map(RedEnvelopeVO::getId).collect(Collectors.toList());
        List<ReItem> items;
        if (!reIds.isEmpty()) {
            ReItemQueryObject itemQo = new ReItemQueryObject(null, reIds, vo.getUid(), ReItemStateEnum.DRAW.getCode(), ReMomentTypeEnum.MOMENT.getCode(), null);
            itemQo.setPageSize(PageResult.MAX_SIZE);
            items = reItemMapper.query(itemQo);
        } else {
            items = new ArrayList<>();
        }
        //去掉已领取红包
        reMoments.removeIf(re -> items.stream().anyMatch(i -> Objects.equals(i.getReId(),re.getId())));*/
        List<RedEnvelopeVO> list = reMomentMapper.query(qo);
        if (isAdmin) {
            list.forEach(r -> {
                r.setReLat(vo.getLat());
                r.setReLng(vo.getLng());
            });
        }
        return list;
    }

    @Override
    public ReMoment viewReMoment(Long reId) {
        ReMoment reMoment = reMomentMapper.selectWithItem(reId);
        if (reMoment == null || ReMomentStateEnum.WAIT.getCode().equals(reMoment.getState())) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        ReItem reItem = reItemMapper.selectOnUserAndRe(new ReItemQueryObject(reId, null, UserContext.getUserId(), ReItemStateEnum.DRAW.getCode(), -1, null,null));
        if (reItem != null) {
            reMoment.setCurrentAmount(reItem.getAmount());
        }
        ReVoteUser voteUser = voteUserMapper.selectByUserAndRe(UserContext.getUserId(), reId);
        reMoment.setVote(voteUser != null && voteUser.isState());
        reMoment.setViewCount(reMoment.getViewCount() + 1);
        reMomentMapper.updateByPrimaryKey(reMoment);
        return reMoment;
    }

    @Override
    public RedEnvelopeVO getSingleRe(Long reId) {
        return reMomentMapper.getSingleRe(reId);
    }

    @Override
    public boolean checkSingleReDraw(Long u1, Long u2) {
        return reMomentMapper.querySingleReDrawCount(u1, u2) > 0;
    }

    @Override
    public ReMoment drawRedEnvelope(DrawRedEnvelopeVO vo) {
        if (vo.getReId().equals(0L)) {
            return this.drawAdRedPacket(vo);
        }
        ReMoment reMoment = reMomentMapper.selectWithItem(vo.getReId());
        if (reMoment == null || ReMomentStateEnum.WAIT.getCode().equals(reMoment.getState())) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        UserStatistics user = userInfoService.getStatisticsByUid(vo.getUid());
        ReItem reItem = reItemMapper.selectOnUserAndRe(new ReItemQueryObject(vo.getReId(), null, user.getUid(), ReItemStateEnum.DRAW.getCode(), -1, null,null));
        if (reItem == null) {//获取明细
            this.assertDrawCondition(reMoment, vo);
            reItem = reItemMapper.selectOnUserAndRe(new ReItemQueryObject(vo.getReId(), null, -1L, ReItemStateEnum.CREATE.getCode(), -1, null,null));
            if (reItem != null) {
                reItem.setState(ReItemStateEnum.DRAW.getCode());
                reItem.setUid(user.getUid());
                reItem.setDrawTime(new Date());
                reItem.setLng(vo.getLng());
                reItem.setLat(vo.getLat());
                this.updateReItem(reItem);
                //处理收入
                this.userAccountService.updateUserAccount(user.getUid(), reMoment.getUid(), reItem.getAmount(), AccountUtil.AccountDealType.DRAW_RED_ENVELOPE, reItem.getId(), reMoment.getUser().getNickname());
                reMoment.setDrawCount(reMoment.getDrawCount() + 1);
                reMoment.setDrawAmount(reMoment.getDrawAmount().add(reItem.getAmount()));
                reMoment.setViewCount(reMoment.getViewCount() + 1);
                if (reMoment.getDrawCount().equals(reMoment.getReNum())) {//判断是否领完
                    reMoment.setState(ReMomentStateEnum.FINISH.getCode());
                }
                this.updateReMoment(reMoment);
                reMoment.getReItems().add(userInfoService.getByUid(user.getUid()));
                //用户领取消息类型
                UserMsgLogTypeEnum msgLogType = UserMsgLogTypeEnum.SINGLE_RE_DRAW;
                if (ReMomentTypeEnum.MOMENT.getCode().equals(reMoment.getType())) {//记录朋友圈红包
                    msgLogType = UserMsgLogTypeEnum.RE_DRAW;
                    user.setReDrawAmount(user.getReDrawAmount().add(reItem.getAmount()));
                    if (reItem.getAmount().compareTo(user.getReBestAmount()) > 0) {
                        //获取最高一次
                        user.setReBestAmount(reItem.getAmount());
                    }
                    user.setReDrawCount(user.getReByDrawCount() + 1);
                    userInfoService.updateUserStatistics(user);
                    //发红包用户
                    UserStatistics userStatistics = reMomentMapper.selectUserStatisticsByReMoment(reItem.getReId());
                    userStatistics.setReByDrawCount(userStatistics.getReByDrawCount() + 1);
                    userInfoService.updateUserStatistics(userStatistics);
                }
                userMsgLogService.save(user.getUid(), reMoment.getUid(), msgLogType, reMoment.getId(), reMoment.getContent());
            }
        }
        ReVoteUser voteUser = voteUserMapper.selectByUserAndRe(user.getUid(), reMoment.getId());
        reMoment.setVote(voteUser != null && voteUser.isState());
        reMoment.setCurrentAmount(reItem != null ? reItem.getAmount() : null);
        return reMoment;
    }

    private ReMoment drawAdRedPacket(DrawRedEnvelopeVO vo) {
        UserStatistics user = userInfoService.getStatisticsByUid(vo.getUid());
        ReItem reItem = new ReItem();
        reItem.setState(ReItemStateEnum.DRAW.getCode());
        reItem.setUid(user.getUid());
        reItem.setDrawTime(new Date());
        reItem.setLng(vo.getLng());
        reItem.setLat(vo.getLat());
        reItem.setType(ReMomentTypeEnum.ACTIVITY.getCode());
        reItem.setAmount(new BigDecimal("0.01"));
        reItemMapper.insert(reItem);
        //处理收入
        this.userAccountService.updateUserAccount(user.getUid(), SystemConstUtil.systemAccountId, reItem.getAmount(), AccountUtil.AccountDealType.DRAW_RED_ENVELOPE, reItem.getId(), "推广广告");
        user.setReDrawAmount(user.getReDrawAmount().add(reItem.getAmount()));
        user.setReDrawCount(user.getReDrawCount() + 1);
        userInfoService.updateUserStatistics(user);
        //返回一个红包对象
        ReMoment reMoment = AdvertisingUtil.getAdReMoment();
        reMoment.setCurrentAmount(reItem.getAmount());
        PageResult<ReItem> items = this.queryReDrawItem(new ReItemQueryObject(null,null,-1L,
                ReItemStateEnum.DRAW.getCode(),ReMomentTypeEnum.ACTIVITY.getCode(),"r.draw_time desc","r.uid"));
        reMoment.setViewCount(items.getTotalCount());
        reMoment.setDrawCount(items.getTotalCount());
        List<UserInfo> users = items.getResult().stream().map(i -> {
            UserInfo u = new UserInfo();
            u.setNickname(i.getNickname());
            u.setHeadUrl(i.getHeadUrl());
            u.setUid(i.getUid());
            return u;
        }).collect(Collectors.toList());
        reMoment.setReItems(users);
        return reMoment;
    }

    /**
     * 断言领取条件
     */
    private void assertDrawCondition(ReMoment reMoment, DrawRedEnvelopeVO vo) {
        UserInfo user = userInfoService.getByUid(vo.getUid());
        if (user.admin()) {
            return;
        }
        //如果不是个人红包，判断是否在范围内
        if (ReMomentTypeEnum.MOMENT.getCode().equals(reMoment.getType())) {
            AddressComponentVO address = AmapUtil.getAddressByLocation(vo.getLng(), vo.getLat());
            if (address == null) {
                throw new BusinessException(CommonErrorResultEnum.LOCATION_ERROR);
            }
            if (ReMomentScopeEnum.ONE_KM.getCode().equals(reMoment.getScope()) && MapDistance.getDistance(vo.getLng(), vo.getLat(), reMoment.getReLng(), reMoment.getReLat()) > 1200
                    || ReMomentScopeEnum.DISTRICT.getCode().equals(reMoment.getScope()) && !address.getAdcode().equals(reMoment.getDistrictId())
                    || ReMomentScopeEnum.CITY.getCode().equals(reMoment.getScope()) && !address.getCitycode().equals(reMoment.getCityId())) {
                throw new BusinessException(CommonErrorResultEnum.RE_OUT_OF_RANGE);
            }
        }
        //如果是创蚁红包
        if (ReMomentCrowdEnum.CREATEANT.getCode().equals(reMoment.getCrowd())) {
            if (!user.getCreateantMember()) {
                throw new BusinessException(CommonErrorResultEnum.RE_NOT_CREATEANT);
            }
        }
        //如果是摇一摇红包且需要限制获取次数
        if (ReMomentCrowdEnum.SHAKE.getCode().equals(reMoment.getCrowd()) && SystemConstUtil.oneUserShakeReDrawTime > 0) {
            //查询用户今天领取的摇一摇红包次数
            ReMomentQueryObject qo = new ReMomentQueryObject();
            qo.setUid(vo.getUid());
            qo.setQueryType(ReMomentQueryObject.QUERY_DRAW_RE);
            qo.setCrowds(new int[]{ReMomentCrowdEnum.SHAKE.getCode()});
            qo.setQueryDate(new Date());
            if (reMomentMapper.queryCount(qo) >= SystemConstUtil.oneUserShakeReDrawTime) {
                throw new BusinessException(CommonErrorResultEnum.RE_DAY_MAX_DRAW,SystemConstUtil.oneUserShakeReDrawTime);
            }
        }
    }

    @Override
    public PageResult<ReItem> queryReDrawItem(ReItemQueryObject qo) {
        int count = reItemMapper.queryCount(qo);
        List<ReItem> list = reItemMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public PageResult<RedEnvelopeVO> queryReMoment(ReMomentQueryObject qo) {
        int count = reMomentMapper.queryCount(qo);
        List<RedEnvelopeVO> list = reMomentMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void addComment(AddReCommentVO vo) {
        ReMoment reMoment = reMomentMapper.selectByPrimaryKey(vo.getReId());
        if (reMoment == null || ReMomentStateEnum.WAIT.getCode().equals(reMoment.getState())) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }

        UserInfo user = userInfoService.getByUid(UserContext.getUserId());
        ReComment c = new ReComment();
        c.setUid(user.getUid());
        c.setReId(vo.getReId());
        c.setContent(vo.getContent());
        Long toUser;
        if (vo.getParentId() != null) {//回复评论
            ReComment comment = reCommentMapper.selectByPrimaryKey(vo.getParentId());
            if (comment == null) {
                throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
            }
            /*if (user.getUid().equals(comment.getUid())) {
                throw new BusinessException(CommonErrorResultEnum.OWN2OWN);
            }*/
            if (comment.getParentId() == null) {//第一级回复
                c.setParentId(comment.getId());
                reCommentMapper.replyCountAddOne(comment.getId());//根评论
            } else {//第二级回复
                c.setReplyUid(comment.getUid());
                c.setParentId(comment.getParentId());
                reCommentMapper.replyCountAddOne(comment.getParentId());//根评论
                reCommentMapper.replyCountAddOne(vo.getParentId());//被评论
            }
            toUser = comment.getUid();
        } else {
            toUser = reMoment.getUid();
        }
        reCommentMapper.insert(c);
        reMoment.setCommentCount(reMoment.getCommentCount() + 1);
        reMomentMapper.updateByPrimaryKey(reMoment);
        userMsgLogService.save(user.getUid(), toUser, UserMsgLogTypeEnum.RE_COMMENT, reMoment.getId(), vo.getContent());
    }

    @Override
    public ReComment getReComment(Long rcId) {
        return this.reCommentMapper.selectByPrimaryKey(rcId);
    }

    @Override
    public PageResult<ReComment> queryReComment(ReCommentQueryObject qo) {
        int count = reCommentMapper.queryCount(qo);
        List<ReComment> list = reCommentMapper.query(qo);
        return new PageResult<>(list, count, qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void handleUserVote(Long reId, boolean vote) {
        ReMoment reMoment = reMomentMapper.selectByPrimaryKey(reId);
        if (reMoment == null || ReMomentStateEnum.WAIT.getCode().equals(reMoment.getState())) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
        ReVoteUser voteUser = voteUserMapper.selectByUserAndRe(UserContext.getUserId(), reId);
        if (voteUser != null) {
            if (vote && !voteUser.isState()) {//点赞操作，且之前没有点赞
                reMoment.setVoteCount(reMoment.getVoteCount() + 1);
            } else if (!vote && voteUser.isState()) {//取消点赞操作且之前有点赞
                reMoment.setVoteCount(reMoment.getVoteCount() - 1);
            }
            voteUser.setState(vote);
            voteUserMapper.updateByPrimaryKey(voteUser);
        } else if (vote) {//点赞才加1
            voteUserMapper.insert(UserContext.getUserId(), reId);
            reMoment.setVoteCount(reMoment.getVoteCount() + 1);
            userMsgLogService.save(UserContext.getUserId(), reMoment.getUid(), UserMsgLogTypeEnum.RE_VOTE, reMoment.getId(), reMoment.getContent());
        }
        reMomentMapper.updateByPrimaryKey(reMoment);
    }

    /**
     * 红包设置在线
     */
    private void setReMomentOnline(Long reId) {
        ReMoment reMoment = reMomentMapper.selectByPrimaryKey(reId);
        if (reMoment == null) {
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST, ReMoment.class.getName());
        }
        reMomentMapper.updateReMomentState(reId, ReMomentStateEnum.NORMAL.getCode());
        userAccountService.updateSysAccount(reMoment.getDrawAmount(), AccountUtil.AccountDealType.SYS_RED_ENVELOPE_FEE, reId, reMoment.getUid().toString());
        if (ReMomentTypeEnum.MOMENT.getCode().equals(reMoment.getType())) {//记录朋友圈红包
            UserStatistics user = userInfoService.getStatisticsByUid(reMoment.getUid());
            //如果是设备红包，添加设备红包金额
            if (ReMomentCrowdEnum.SHAKE.getCode().equals(reMoment.getCrowd())) {
                user.setDeviceRePayAmount(user.getDeviceRePayAmount().add(reMoment.getReAmount()));
            }
            user.setRePayAmount(user.getRePayAmount().add(reMoment.getReAmount()));
            user.setRePayCount(user.getRePayCount() + 1);
            userInfoService.updateUserStatistics(user);
        }
        this.createReItem(reMoment);
    }

    /**
     * 更新红包明细
     *
     * @param reItem
     */
    private void updateReItem(ReItem reItem) {
        if (reItemMapper.updateByPrimaryKey(reItem) <= 0) {
            throw new BusinessException(CommonErrorResultEnum.OPTIMISTIC_LOCK_ERROR);
        }
    }

    private ReOrder createReOrder(Long uid, Long reId, BigDecimal amount) {
        ReOrder order = new ReOrder();
        OrderUtil.setOrderBaseParam(order, uid, amount, OrderUtil.OrderType.RE_MOMENT);
        order.setReId(reId);
        reOrderMapper.insert(order);
        baseOrderService.createBaseOrder(order);
        return order;
    }


    /**
     * 生成红包明细
     */
    private void createReItem(ReMoment reMoment) {
        List<Integer> splitRedPackets = RedEnvelopesUtil.splitRedPackets(NumberUtil.fromYuan2Fen(reMoment.getReAmount().subtract(reMoment.getDrawAmount())), reMoment.getReNum());
        List<ReItem> list = splitRedPackets.stream().map(i -> {
            ReItem item = new ReItem();
            item.setState(ReItemStateEnum.CREATE.getCode());
            item.setReId(reMoment.getId());
            item.setAmount(NumberUtil.fromFen2Yuan(i));
            item.setType(reMoment.getType());
            return item;
        }).collect(Collectors.toList());
        if (!list.isEmpty()) {
            reItemMapper.insertBatch(list);
        }
    }
}
