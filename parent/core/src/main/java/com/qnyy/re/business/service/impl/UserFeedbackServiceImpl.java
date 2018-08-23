package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.enums.AuditStatusEnum;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.DateUtil;
import com.qnyy.re.base.util.UserContext;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.UserFeedback;
import com.qnyy.re.business.mapper.UserFeedbackMapper;
import com.qnyy.re.business.service.IUserFeedbackService;
import com.qnyy.re.business.vo.param.SaveFeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by E_Iva on 2017.12.19.0019.
 */
@Service
public class UserFeedbackServiceImpl implements IUserFeedbackService {
    @Autowired
    private UserFeedbackMapper mapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Override
    public void saveFeedback(SaveFeedbackVO vo) {
        UserFeedback f = mapper.selectByUserAtLatest(vo.getUid());
        if (f != null && DateUtil.intervalTime(new Date(), f.getCreated(), DateUtil.MINUTE) <= 5) {
            throw new BusinessException(CommonErrorResultEnum.FEEDBACK_OFTEN);
        }
        UserFeedback feedback = new UserFeedback();
        feedback.setState(AuditStatusEnum.CREATE.getCode());
        feedback.setContent(vo.getContent());
        feedback.setEmail(vo.getEmail());
        feedback.setPhone(vo.getPhone());
        feedback.setQq(vo.getQq());
        feedback.setIp(vo.getIp());
        feedback.setApplierId(vo.getUid());
        mapper.insert(feedback);
        userInfoService.addUserStatisticsCount(vo.getUid(), UserStatistics.CountField.FEEDBACK_COUNT);
    }
}
