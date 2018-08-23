package com.qnyy.re.base.util.container;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by E_Iva on 2017.12.5.0005.
 */
@Getter@Setter
abstract public class BaseEntity implements Serializable{
    private static final long serialVersionUID = -4047497606198200125L;
    protected Long id;

    /**
     * 解析地理信息
     * @param point
     * @return
     */
    protected String[] parsePoint(String point){
        if (point != null) {
            int i = point.indexOf("(");
            String lnl = point.substring(i + 1, point.length() - 1);
            return lnl.split(" ");
        }
        return null;
    }
}
