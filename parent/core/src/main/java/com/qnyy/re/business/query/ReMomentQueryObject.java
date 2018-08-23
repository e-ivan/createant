package com.qnyy.re.business.query;

import com.qnyy.re.base.util.container.QueryObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by E_Iva on 2017.12.7.0007.
 */
@Setter@Getter
public class ReMomentQueryObject extends QueryObject {
    public static final Integer QUERY_DRAW_RE = 0;//查询领取红包
    public static final Integer QUERY_PAY_RE = 1;//查询发送红包
    private Long uid = -1L;
    private int[] states;
    private int queryType;
    private int[] types;//红包类型
    private int[] crowds;//领取人群
    private String lng;
    private String lat;
    private int distance = 1000;//距离（米）
    private int cityId = -1;
    private int districtId = -1;
    private List<Long> reIds;//红包id集合
    private String orderBy; //排序规则
    private Date queryDate; //查询日期
    private Long storeId;//店铺Id
    private Long removeUid = -1L; //去除用户id
}
