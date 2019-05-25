package com.qnyy.re.base.listener;

import com.qnyy.re.base.enums.ApiInfoSite;
import com.qnyy.re.base.service.IApiInfoService;
import com.qnyy.re.base.service.ILoginInfoService;
import com.qnyy.re.base.service.IMchService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 初始化系统账户监听器
 * Created by E_Iva on 2018.1.8.0008.
 */
@Component
public class InitSystemAccountListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IApiInfoService apiInfoService;
    @Autowired
    private IMchService mchService;

    private static boolean updateApi = false;

    @Setter
    private RequestMappingHandlerMapping handlerMapping;
    @Setter
    private ApiInfoSite site;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (handlerMapping != null && !updateApi) {
            apiInfoService.updateApiInfo(handlerMapping,site);
            updateApi = true;
            System.out.println("===============更新接口信息完毕===============");
        }
        //当使用springMVC时，容器会初始化两次，判断容器没有父级时，说明为最大的容器
        if (event.getApplicationContext().getParent() == null) {
            loginInfoService.initSystemAccount();
            mchService.iniSystemMch();
            System.out.println("================初始化系统帐户完毕==============");
        }
    }
}
