package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.entity.UserAccount;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.AccountUtil;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.SysPromoReap;
import com.qnyy.re.business.entity.UserClassRelation;
import com.qnyy.re.business.mapper.SysPromoReapMapper;
import com.qnyy.re.business.mapper.UserClassRelationMapper;
import com.qnyy.re.business.query.UserClassRelationQueryObject;
import com.qnyy.re.business.service.IUserClassRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by E_Iva on 2017.12.18.0018.
 */
@Service
public class UserClassRelationServiceImpl implements IUserClassRelationService {
    @Autowired
    private UserClassRelationMapper mapper;
    @Autowired
    private SysPromoReapMapper sysPromoReapMapper;
    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void setReapRatio(BigDecimal firstReap, BigDecimal secondReap) {
        sysPromoReapMapper.insert(new SysPromoReap(firstReap,secondReap));
    }

    @Override
    public SysPromoReap getReapRatio() {
        SysPromoReap reap = sysPromoReapMapper.selectLatest();
        if (reap == null) {
            reap = new SysPromoReap(new BigDecimal("0.03"),new BigDecimal("0.01"));
        }
        return reap;
    }

    @Override
    public void createRelation(Long upper, Long junior) {
        if (upper.equals(junior)) {
            throw new BusinessException(CommonErrorResultEnum.OWN2OWN);
        }
        UserAccount upperUser = userAccountService.getUserAccountByUser(upper);
        if (upperUser == null) {
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        if (mapper.selectByJunior(junior) != null) {
            throw new BusinessException(CommonErrorResultEnum.USER_HAD_UPPER);
        }
        if (mapper.queryUpperCount(junior) > 0) {
            throw new BusinessException(CommonErrorResultEnum.USER_HAD_JUNIOR);
        }
        userInfoService.addUserStatisticsCount(upper, UserStatistics.CountField.FIRST_RELATION_COUNT);
        UserClassRelation user = mapper.selectByJunior(upper);
        if (user != null) {
            userInfoService.addUserStatisticsCount(user.getUpper(), UserStatistics.CountField.SECOND_RELATION_COUNT);
        }
        mapper.insert(new UserClassRelation(upper,junior));
    }

    @Override
    public void countReap(Long uid, BigDecimal amount) {
        //获取上级
        UserClassRelation firstRelation = mapper.selectByJunior(uid);
        if (firstRelation != null) {
            SysPromoReap reapRatio = this.getReapRatio();
            BigDecimal firstReap = amount.multiply(reapRatio.getFirstReapRatio());
            BigDecimal secondReap = amount.multiply(reapRatio.getSecondReapRatio());
            //处理第一级
            userAccountService.updateUserAccount(firstRelation.getUpper(),firstReap, AccountUtil.AccountDealType.FIRST_RELATION_REAP);
            firstRelation.setReap(firstRelation.getReap().add(firstReap));
            UserStatistics firstStatistics = userInfoService.getStatisticsByUid(firstRelation.getUpper());
            firstStatistics.setTotalRelationReap(firstStatistics.getTotalRelationReap().add(firstReap));
            firstStatistics.setFirstRelationReap(firstStatistics.getFirstRelationReap().add(firstReap));
            userInfoService.updateUserStatistics(firstStatistics);

            UserClassRelation secondRelation = mapper.selectByJunior(firstRelation.getUpper());
            if (secondRelation != null) {
                //处理第二级
                firstRelation.setSecondReap(firstRelation.getSecondReap().add(secondReap));
                userAccountService.updateUserAccount(secondRelation.getUpper(),secondReap, AccountUtil.AccountDealType.SECOND_RELATION_REAP);
                secondRelation.setReap(secondRelation.getReap().add(secondReap));
                mapper.updateByPrimaryKey(secondRelation);
                UserStatistics secondStatistics = userInfoService.getStatisticsByUid(secondRelation.getUpper());
                secondStatistics.setTotalRelationReap(secondStatistics.getTotalRelationReap().add(secondReap));
                userInfoService.updateUserStatistics(secondStatistics);
            }
            mapper.updateByPrimaryKey(firstRelation);
        }
    }

    @Override
    public PageResult<UserClassRelation> queryReap(UserClassRelationQueryObject qo) {
        int count = mapper.queryCount(qo);
        List<UserClassRelation> list = mapper.query(qo);
        return new PageResult<>(list,count,qo.getCurrentPage(),qo.getPageSize());
    }
}
