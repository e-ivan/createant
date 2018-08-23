package com.qnyy.re.business.entity;

import com.alibaba.fastjson.JSON;
import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.util.container.BaseEntity;
import com.qnyy.re.business.util.CreateantRequestUtil;
import com.qnyy.re.business.vo.StoreVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter@Getter
public class ReMoment extends BaseEntity {
    private Long uid;

    @JsonIgnore
    private String picUrl;

    private UserInfo user;

    private Integer state;

    private BigDecimal reAmount;

    private Integer reNum;

    private BigDecimal drawAmount = BigDecimal.ZERO;

    private Integer drawCount = 0;

    private BigDecimal currentAmount;//当前获取金额

    private Date created;

    private Timestamp updated;

    @JsonIgnore
    private String location;

    @JsonIgnore
    private String reLocation;

    private Integer scope = 0;

    private Integer districtId = 0;

    private Integer cityId = 0;

    private Integer crowd;

    private String linkTo;

    private Long storeId;   //门店ID

    private Long shopId;    //店铺ID

    private String content;

    private Integer commentCount;

    private Integer viewCount;

    private Integer voteCount;

    private List<UserInfo> reItems;

    private boolean isVote;

    @JsonIgnore
    private Integer type;

    public void setReLocation(String reLocation){
        this.reLocation = reLocation;
        //获取经纬度
        if (reLocation != null) {
            String[] num = parsePoint(reLocation);
            this.reLng = num[0];
            this.reLat = num[1];
        }
    }

    @JsonIgnore
    private String userLng;//用户经度
    @JsonIgnore
    private String userLat;//用户纬度
    private String reLng;//红包经度
    private String reLat;//红包纬度

    private Integer version;

    public List<UploadFile> getImages(){
        if (StringUtils.isBlank(picUrl)){
            return new ArrayList<>();
        }
        return JSON.parseArray(picUrl,UploadFile.class);
    }

    public StoreVO getStoreInfo() {
        if (this.storeId != null) {
            return CreateantRequestUtil.getStoreInfo(storeId, true);
        }
        return null;
    }

}