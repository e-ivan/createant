package com.qnyy.re.business.util;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.business.entity.ReMoment;
import com.qnyy.re.business.enums.ReMomentStateEnum;
import com.qnyy.re.business.enums.ReMomentTypeEnum;
import com.qnyy.re.business.vo.RedEnvelopeVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * 广告工具类
 */
@Component
public class AdvertisingUtil {
    private static String advertiserName = "广告商";
    private static String advertiserHeadUrl = "/headUrl.jpg";
    private static String advertisingContent = "广告红包内容";
    private static String advertisingDate = "2018-7-30";

    public static UserInfo getAdUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setHeadUrl(advertiserHeadUrl);
        userInfo.setNickname(advertiserName);
        return userInfo;
    }

    public static ReMoment getAdReMoment() {
        ReMoment reMoment = new ReMoment();
        reMoment.setUser(getAdUserInfo());
        reMoment.setType(ReMomentTypeEnum.ACTIVITY.getCode());
        reMoment.setState(ReMomentStateEnum.NORMAL.getCode());
        try {
            reMoment.setCreated(DateUtils.parseDate(advertisingDate,"yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reMoment.setContent(advertisingContent);
        return reMoment;
    }

    public static RedEnvelopeVO getRedEnvelope() {
        RedEnvelopeVO vo = new RedEnvelopeVO();
        vo.setUser(getAdUserInfo());
        vo.setContent(advertisingContent);
        try {
            vo.setCreated(DateUtils.parseDate(advertisingDate,"yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vo.setState(ReMomentStateEnum.NORMAL.getCode());
        vo.setUid(vo.getUser().getUid());
        vo.setId(0L);
        return vo;
    }
}
