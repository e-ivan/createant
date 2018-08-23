package com.qnyy.re.base.util.container;

import lombok.Getter;
import lombok.Setter;

/**
 * dly
 */
@Getter@Setter
public class ObjectResponse<T> extends Response {

    private T data;

    public ObjectResponse(T object) {
        this.data = object;
    }
    public ObjectResponse(T object,String msg) {
        this.data = object;
        this.msg = msg;
    }


}
