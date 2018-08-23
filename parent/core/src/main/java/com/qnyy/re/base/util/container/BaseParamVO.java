package com.qnyy.re.base.util.container;

import com.qnyy.re.base.enums.CommonErrorResultEnum;
import com.qnyy.re.base.util.annotation.VerifyParam;
import com.qnyy.re.base.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by E_Iva on 2017/11/28.
 */
abstract public class BaseParamVO implements Serializable {
    private static final long serialVersionUID = -1782819779741234789L;

    private Map<String, List<String>> manyVerifyMap = new HashMap<>();
    private Map<String, Boolean> fieldParamFullMap = new HashMap<>();

    public void checkParamFull(JoinPoint point) {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                VerifyParam verifyParam = field.getAnnotation(VerifyParam.class);
                if (verifyParam != null) {
                    boolean skip = false;//是否跳过普通检查
                    boolean noNull;//该字段是否有值
                    //不检查空值的方法
                    String[] methods = verifyParam.unCheckMethod();
                    if (point != null && Arrays.asList(methods).contains(point.getSignature().getName())) {
                        continue;
                    }
                    if (StringUtils.isNoneBlank(verifyParam.value())) {
                        List<String> fieldList = manyVerifyMap.computeIfAbsent(verifyParam.value(), fl -> new ArrayList<>());
                        fieldList.add(field.getName());
                        skip = true;
                    }
                    customCheckField(field);
                    if (field.getType().equals(String.class)) {
                        noNull = StringUtils.isNoneBlank((String) field.get(this));
                    } else {
                        noNull = field.get(this) != null;
                    }
                    fieldParamFullMap.put(field.getName(), noNull);
                    if (!skip && !noNull) {
                        throw new RuntimeException(field.getName());
                    }
                }
            }
            //处理同等级的参数(多选一)
            manyVerifyMap.values().forEach(fields -> {
                List<String> params = fields.stream().filter(fieldParamFullMap::get).collect(Collectors.toList());
                if (params.size() < 1) {
                    throw new RuntimeException(StringUtils.join(fields, " or "));
                } else if (params.size() > 1) {
                    throw new RuntimeException("param conflicting:" + StringUtils.join(params, " and "));
                }
            });
        } catch (Exception e) {
            throw new BusinessException(CommonErrorResultEnum.REQUEST_PARAM_LACK, e.getMessage());
        }
    }

    /**
     * 是否为URL
     *
     * @param str
     * @return
     */
    public boolean isUrl(String str) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://|[fF][tT][pP]://).*");
        return str != null && pattern.matcher(str).matches();
    }

    //自定义字段检查
    protected void customCheckField(Field field) {
        try {
            this.getClass().getDeclaredMethod("customCheckField", Field.class);
            //子类有覆盖方法
            System.out.println("execute custom check filed method");
        } catch (NoSuchMethodException e) {

        }
    }
}
