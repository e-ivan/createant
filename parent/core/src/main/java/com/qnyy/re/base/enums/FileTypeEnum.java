package com.qnyy.re.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型路径
 * Created by dly on 2016/8/12.
 */
@Getter@AllArgsConstructor
public enum FileTypeEnum {
    IMAGE("image", "/image"),
    APP("app", "/app"),
    VIDEO("video", "/video"),
    VOICE("voice", "/voice");

    private String type;
    private String path;
    public static String getPathByType(String type){
        for(FileTypeEnum fileTypeAndPath: FileTypeEnum.values()){
            if (fileTypeAndPath.getType().equals(type)){
                return fileTypeAndPath.getPath();
            }
        }
        return null;
    }
}
