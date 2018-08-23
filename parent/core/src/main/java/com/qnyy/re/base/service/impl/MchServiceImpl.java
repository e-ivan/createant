package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.ApiInfo;
import com.qnyy.re.base.entity.MchInfo;
import com.qnyy.re.base.enums.ApiInfoSite;
import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.enums.MchInfoState;
import com.qnyy.re.base.mapper.ApiInfoMapper;
import com.qnyy.re.base.mapper.MchInfoMapper;
import com.qnyy.re.base.service.IMchService;
import com.qnyy.re.base.util.CodeUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by E_Iva on 2018.2.8.0008.
 */
@Service
@Slf4j
public class MchServiceImpl implements IMchService {
    @Autowired
    private MchInfoMapper mapper;
    @Autowired
    private ApiInfoMapper apiInfoMapper;
    @Override
    public void createMch(MchInfo info) {
        MchInfo m = new MchInfo();
        m.setMchName(info.getMchName());
        m.setMchId(info.getMchId());
        m.setTokenSource(info.getTokenSource());
        m.setMchKey(CodeUtil.getStringRandom(12));
        m.setState(MchInfoState.NORMAL.getCode());
        try {
            mapper.insert(m);
        }catch (DuplicateKeyException e){
            throw new BusinessException(CommonErrorResultEnum.MCH_EXIST);
        }
    }

    @Override
    public void setMchApiPermissions(String mchId, List<Long> apiIds) {
        int i = mapper.updateApiPermissions(mchId, StringUtils.join(apiIds, "|"));
        if (i <= 0){
            throw new BusinessException(CommonErrorResultEnum.OBJECT_UN_EXIST);
        }
    }

    @Override
    public MchInfo getByMchId(String mchId) {
        return mapper.selectByMchId(mchId);
    }

    @Override
    public void iniSystemMch() {
        try {
            this.createMch(new MchInfo(SystemConstUtil.systemMchName,SystemConstUtil.systemMchId,0));
            log.debug("初始化系统MCH账户成功！");
        }catch (BusinessException e){
            log.debug("账户已初始化");
        }finally {
            //设置接口权限
            List<ApiInfo> appApis = apiInfoMapper.selectAllBySite(ApiInfoSite.APP.getValue());
            List<ApiInfo> mgrApis = apiInfoMapper.selectAllBySite(ApiInfoSite.MGR.getValue());
            List<Long> apiIds = Stream.concat(appApis.stream(), mgrApis.stream()).map(ApiInfo::getId).collect(Collectors.toList());
            this.setMchApiPermissions(SystemConstUtil.systemMchId,apiIds);
            System.out.println("==========更新系统帐户权限接口完成==========");
        }
    }
}
