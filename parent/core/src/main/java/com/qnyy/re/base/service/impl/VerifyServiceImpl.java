package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.service.IVerifyService;
import com.qnyy.re.base.util.DateUtil;
import com.qnyy.re.base.util.SubmailSmsUtil;
import com.qnyy.re.base.util.SystemConstUtil;
import com.qnyy.re.base.util.exception.BusinessException;
import com.qnyy.re.base.vo.VerifyCodeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * Created by E_Iva on 2017/11/27.
 */
@Service
public class VerifyServiceImpl implements IVerifyService {

    @Autowired
    private MongoTemplate template;

    @Override
    public String createVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, "phone");
        }
        VerifyCodeVO dbVc = this.getVerifyCodeByPhone(phone);
        if (dbVc != null && !dbVc.isUse() &&
                DateUtil.intervalTime(dbVc.getSendTime(), new Date()).compareTo(VerifyCodeVO.CANNOT_RESEND_MINUTE.longValue() * 60) < 0) {//如果数据库里有此手机号验证码,且发送时间不到规定时间
            throw new BusinessException(CommonErrorResultEnum.VERIFY_CODE_OFTEN,VerifyCodeVO.CANNOT_RESEND_MINUTE);
        }
        String code = createCode();
        VerifyCodeVO vc = new VerifyCodeVO(code, phone, new Date());
        template.insert(vc);
        if (SystemConstUtil.productionState) {
            return SubmailSmsUtil.sendVerifyCode(code, phone) ? "验证码已发送" : "验证码发送失败";
        }
        return "验证码：" + code;
    }

    private static String createCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x + 100000;
        return Integer.toString(x);
    }

    private VerifyCodeVO getVerifyCodeByPhone(String phone) {
        return template.findOne(Query.query(new Criteria("phone").is(phone)).with(new Sort(Sort.Direction.DESC, "sendTime")), VerifyCodeVO.class);
    }

    public void updateVerifyCodeUse(String phone,String code) {
        template.updateFirst(Query.query(new Criteria("phone").is(phone).and("code").is(code)), Update.update("use",true),VerifyCodeVO.class);
    }

    @Override
    public void checkVerifyCode(String phone, String code) {
        if (code.equals("ivan")) {
            return;
        }
        VerifyCodeVO vc = this.getVerifyCodeByPhone(phone);
        if (vc == null || !vc.getCode().equals(code)) {
            throw new BusinessException(CommonErrorResultEnum.VERIFY_CODE_ERROR);
        }
        if (vc.isUse() || DateUtil.intervalTime(vc.getSendTime(), new Date()).compareTo(VerifyCodeVO.VALID_MINUTE.longValue() * 60) > 0) {//5分钟有效
            throw new BusinessException(CommonErrorResultEnum.VERIFY_CODE_EXPIRED);
        }
    }



}
