package com.qnyy.re.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.qnyy.re.base.util.container.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 周边设备页面
 */
@Getter@Setter
public class ShakearoundPage extends BaseEntity {

    @JSONField(name = "page_id")
    private Integer pageId;

    private String title;

    private String description;

    @JSONField(name = "page_url")
    private String pageUrl;

    private String comment;

    @JSONField(name = "icon_url")
    private String iconUrl;

    private Long storeId;

    private Long uid;

}