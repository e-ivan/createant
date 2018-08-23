package com.qnyy.re.base.util.exception;

import com.qnyy.re.base.util.container.BaseParamVO;
import org.aspectj.lang.JoinPoint;

/**
 * aop工具
 * Created by E_Iva on 2017.12.21.0021.
 */
public class AopUtil {
    public void checkParam(JoinPoint point){
        //获取方法参数
        for (Object o : point.getArgs()) {
            if (o instanceof BaseParamVO) {
                ((BaseParamVO) o).checkParamFull(point);
            }
        }
    }
}
