package com.qnyy.re.business.service;

import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.business.entity.SysPromoReap;
import com.qnyy.re.business.entity.UserClassRelation;
import com.qnyy.re.business.query.UserClassRelationQueryObject;

import java.math.BigDecimal;

/**
 * 用户阶级关系服务
 * Created by E_Iva on 2017.12.18.0018.
 */
public interface IUserClassRelationService {

    /**
     * 设置收益比例
     * @param firstReap     一级比例
     * @param secondReap    二级比例
     */
    void setReapRatio(BigDecimal firstReap,BigDecimal secondReap);

    SysPromoReap getReapRatio();
    /**
     * 创建关系
     */
    void createRelation(Long upper,Long junior);

    /**
     * 计算收益
     */
    void countReap(Long uid, BigDecimal amount);

    /**
     * 查询收益
     */
    PageResult<UserClassRelation> queryReap(UserClassRelationQueryObject qo);

}
