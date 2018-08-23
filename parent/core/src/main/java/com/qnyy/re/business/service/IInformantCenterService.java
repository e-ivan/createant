package com.qnyy.re.business.service;

import com.qnyy.re.business.vo.param.CreateInformantVO;

/**
 * 用户举报中心
 * Created by E_Iva on 2017.12.19.0019.
 */
public interface IInformantCenterService {
    /**
     * 创建举报
     */
    void createInform(CreateInformantVO vo);
}
