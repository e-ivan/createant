package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.ApiInfo;
import com.qnyy.re.base.enums.ApiInfoSite;
import com.qnyy.re.base.mapper.ApiInfoMapper;
import com.qnyy.re.base.service.IApiInfoService;
import com.qnyy.re.base.util.annotation.ApiDocument;
import com.qnyy.re.base.util.annotation.UnRequiredLogin;
import com.qnyy.re.base.util.container.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by E_Iva on 2018.2.8.0008.
 */
@Service
public class ApiInfoServiceImpl implements IApiInfoService {
    @Autowired
    private ApiInfoMapper mapper;

    @Override
    public void updateApiInfo(RequestMappingHandlerMapping handlerMapping,ApiInfoSite site) {
        //查询所有已保存接口
        List<ApiInfo> apiInfos = mapper.selectAllBySite(site.getValue());
        Map<String, ApiInfo> map = apiInfos.stream().collect(Collectors.toMap(ApiInfo::getUri, apiInfo -> apiInfo));
        //获取springMvc中的接口方法
        List<ApiInfo> insertList = new ArrayList<>();
        List<ApiInfo> updateList = new ArrayList<>();
        List<ApiInfo> allList = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> methodMap = handlerMapping.getHandlerMethods();
        methodMap.forEach((mapping, method) -> mapping.getPatternsCondition().getPatterns().forEach(urlPattern -> {
            //获取接口方法上的注解以及接口所在类的注解
            ApiDocument methodDocument = method.getMethodAnnotation(ApiDocument.class);
            if (methodDocument != null) {
                ApiInfo apiInfo = new ApiInfo();
                apiInfo.setName(methodDocument.value());
                apiInfo.setIntro(methodDocument.intro());
                RequestMapping rm = method.getBeanType().getAnnotation(RequestMapping.class);
                if (rm != null) {
                    String type = StringUtils.join(rm.value(), ",");
                    apiInfo.setType(StringUtils.removeStart(type,"/"));
                }
                RequestMapping re = method.getMethodAnnotation(RequestMapping.class);
                if (re != null) {
                    apiInfo.setRequestMethod(StringUtils.defaultIfBlank(StringUtils.join(re.method(), ","),"ALL"));
                }
                UnRequiredLogin unRequiredLogin = method.getMethodAnnotation(UnRequiredLogin.class);
                if (unRequiredLogin != null) {
                    apiInfo.setRequiredLogin(false);
                    apiInfo.setCheckSign(unRequiredLogin.checkSign());
                } else {
                    apiInfo.setRequiredLogin(true);
                    apiInfo.setCheckSign(true);
                }
                apiInfo.setBeanClass(method.getBeanType().getName());
                apiInfo.setMethod(method.getMethod().getName());
                apiInfo.setUri(urlPattern);
                apiInfo.setSite(site.getValue());
                allList.add(apiInfo);
                ApiInfo dbApi = map.get(apiInfo.getUri());
                if (dbApi == null) {
                    insertList.add(apiInfo);
                } else if (!dbApi.equals(apiInfo)) {//有修改
                    updateList.add(apiInfo);
                    allList.add(dbApi);//添加原来的数据，不被误删
                }
            }
        }));
        if (updateList.size() > 0) {
            mapper.batchUpdate(updateList);
        }
        if (insertList.size() > 0) {
            mapper.batchInsert(insertList);
        }
        apiInfos.removeAll(allList);
        List<Long> ids = apiInfos.stream().map(BaseEntity::getId).collect(Collectors.toList());
        if (ids.size() > 0) {
            mapper.batchDelete(ids);
        }

    }

    @Override
    public ApiInfo getByUri(String uri,String site) {
        return mapper.selectByUri(uri,site);
    }
}
