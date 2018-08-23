package com.qnyy.re.base.entity;

import com.qnyy.re.base.util.container.BaseUserEntity;
import lombok.Getter;
import lombok.Setter;
@Setter@Getter
public class UploadFile extends BaseUserEntity {

    private Long objectId;

    private String purpose;

    private String path;

    private String type;

    private Integer height;

    private Integer width;
}