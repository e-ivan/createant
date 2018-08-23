package com.qnyy.re.business.service;

import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.business.entity.*;
import com.qnyy.re.business.query.ReCommentQueryObject;
import com.qnyy.re.business.query.ReItemQueryObject;
import com.qnyy.re.business.query.ReMomentQueryObject;
import com.qnyy.re.business.vo.RedEnvelopeVO;
import com.qnyy.re.business.vo.param.*;

import java.util.List;

/**
 * 红包朋友圈服务
 * Created by E_Iva on 2017.11.29 0029.
 */
public interface IReMomentService {
    /**
     * 创建红包
     */
    ReOrder createReMoment(CreateReMomentVO vo);

    /**
     * 创建单个红包
     */
    ReOrder createSingleRe(CreateSingleReVO vo);

    /**
     * 根据orderId获取订单
     */
    ReOrder getByOrderId(String orderId);

    /**
     * 订单设置为成功
     */
    ReOrder setOrderSuccess(String orderId);

    /**
     * 更新红包订单
     */
    void updateReOrder(BaseOrder reOrder);

    /**
     * 查询附近红包
     */
    List<RedEnvelopeVO> queryNearRe(QueryNearbyRedEnvelopeVO vo);

    /**
     * 查看红包
     */
    ReMoment viewReMoment(Long reId);

    /**
     * 获取单个红包
     */
    RedEnvelopeVO getSingleRe(Long reId);

    /**
     * 查询两个用户是否有领取过红包
     * @param u1
     * @param u2
     */
    boolean checkSingleReDraw(Long u1,Long u2);

    /**
     * 领取红包
     */
    ReMoment drawRedEnvelope(DrawRedEnvelopeVO vo);

    /**
     * 查询红包领取明细
     */
    PageResult<ReItem> queryReDrawItem(ReItemQueryObject qo);

    /**
     * 查询红包记录
     */
    PageResult<RedEnvelopeVO> queryReMoment(ReMomentQueryObject qo);

    /**
     * 添加红包评论
     */
    void addComment(AddReCommentVO vo);

    /**
     * 获取指定评论
     */
    ReComment getReComment(Long rcId);

    /**
     * 查询红包评论
     */
    PageResult<ReComment> queryReComment(ReCommentQueryObject qo);

    /**
     * 操作点赞
     * @param vote true 点赞  false 取消点赞
     */
    void handleUserVote(Long reId,boolean vote);
}
