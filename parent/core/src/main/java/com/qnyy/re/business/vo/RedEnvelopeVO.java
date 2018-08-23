package com.qnyy.re.business.vo;

import com.alibaba.fastjson.JSON;
import com.qnyy.re.base.entity.UploadFile;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 红包对象
 * Created by E_Iva on 2017.12.9.0009.
 */
@Getter@Setter
public class RedEnvelopeVO extends BaseEntity {
    private Long id;

    private Long uid;

    @JsonIgnore
    private String picUrl;

    private Integer scope;

    private Integer commentCount;

    private Integer viewCount;

    private Integer voteCount;

    private Date created;

    private Integer state;

    private UserInfo user;

    private Integer crowd;
    @JsonIgnore
    private String reLocation;

    public void setReLocation(String reLocation){
        this.reLocation = reLocation;
        //获取经纬度
        if (reLocation != null) {
            String[] num = parsePoint(reLocation);
            this.reLng = num[0];
            this.reLat = num[1];
        }
    }

    public List<UploadFile> getImages(){
        if (StringUtils.isBlank(picUrl)){
            return new ArrayList<>();
        }
        return JSON.parseArray(picUrl,UploadFile.class);
    }
    private String reLng;//红包经度
    private String reLat;//红包纬度
    private String content;
    private Integer drawState = 0;
    private Date drawTime;//领取时间
}
