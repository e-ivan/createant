package com.qnyy.re.business.service.impl;

import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.enums.AuditStatusEnum;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IUserInfoService;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.business.entity.InformantCenter;
import com.qnyy.re.business.mapper.InformantCenterMapper;
import com.qnyy.re.business.service.IInformantCenterService;
import com.qnyy.re.business.vo.param.CreateInformantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Created by E_Iva on 2017.12.19.0019.
 */
@Service
public class InformantCenterServiceImpl implements IInformantCenterService {
    @Autowired
    private InformantCenterMapper mapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Override
    public void createInform(CreateInformantVO vo) {
        if (vo.getApplierId().equals(vo.getUid())) {
            throw new BusinessException(CommonErrorResultEnum.OWN2OWN);
        }
        UserStatistics statistics = userInfoService.getStatisticsByUid(vo.getUid());
        if (statistics == null){
            throw new BusinessException(CommonErrorResultEnum.USER_UN_EXIST);
        }
        InformantCenter i = new InformantCenter();
        i.setApplierId(vo.getApplierId());
        i.setDefendant(vo.getUid());
        i.setQq(vo.getQq());
        i.setPhone(vo.getPhone());
        i.setContent(vo.getContent());
        i.setEmail(vo.getEmail());
        i.setType(vo.getType());
        i.setIp(vo.getIp());
        i.setState(AuditStatusEnum.CREATE.getCode());
        try {
            mapper.insert(i);
        }catch (DuplicateKeyException e){
            throw new BusinessException(CommonErrorResultEnum.INFORM_OFTEN);
        }
        userInfoService.addUserStatisticsCount(vo.getApplierId(), UserStatistics.CountField.INFORM_COUNT);
        statistics.setByInformCount(statistics.getByInformCount() + 1);
        userInfoService.updateUserStatistics(statistics);
    }
}
