package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.ApiInfo;
import com.qnyy.re.base.enums.ApiInfoSite;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 接口文档服务
 * Created by E_Iva on 2018.2.8.0008.
 */
public interface IApiInfoService {
    /**
     * 更新api
     * @param handlerMapping    请求映射处理器
     * @param site  端
     */
    void updateApiInfo(RequestMappingHandlerMapping handlerMapping, ApiInfoSite site);
    ApiInfo getByUri(String uri,String site);
}
