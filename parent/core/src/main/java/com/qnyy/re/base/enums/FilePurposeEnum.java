package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件用途
 * Created by E_Iva on 2017.11.30 0030.
 */
@Getter@AllArgsConstructor
public enum FilePurposeEnum {
    RE_MOMENT("红包图片","reMoment",250D),
    USER_HEAD_IMG("用户头像","userHeadImg",120D),

    ;

    public static String getPathByType(String type){
        for(FilePurposeEnum fileTypeAndPath: FilePurposeEnum.values()){
            if (fileTypeAndPath.getPath().equals(type)){
                return fileTypeAndPath.getPath();
            }
        }
        return null;
    }

    private String type;
    private String path;
    private Double smallHeight;//压缩高度
}
