package com.qnyy.re.test;

import com.qnyy.re.base.entity.LoginInfo;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.mapper.ApiInfoMapper;
import com.qnyy.re.base.mapper.LoginInfoMapper;
import com.qnyy.re.base.mapper.UserInfoMapper;
import com.qnyy.re.base.query.UserInfoQueryObject;
import com.qnyy.re.base.service.IMchService;
import com.qnyy.re.base.service.impl.ApiInfoServiceImpl;
import com.qnyy.re.base.service.impl.UserInfoServiceImpl;
import com.qnyy.re.base.util.BitStateUtil;
import com.qnyy.re.business.util.CreateantRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by hh on 2017.8.22 0022.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-mvc.xml")
public class SpringTest {

    @Autowired
    private ApiInfoServiceImpl service;
    @Autowired
    private ApiInfoMapper apiInfoMapper;
    @Autowired
    private IMchService mchService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserInfoServiceImpl userInfoService;
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Test
    public void testddg() throws Exception {

    }
    @Test
    public void testTT() throws Exception {
        UserInfoQueryObject qo = new UserInfoQueryObject();
        qo.setPageSize(Integer.MAX_VALUE);
        List<UserInfo> query = userInfoMapper.query(qo);

        query.stream().filter(u -> !u.getBindPhone() && StringUtils.isNoneBlank(loginInfoMapper.selectByPrimaryKey(u.getUid()).getPhone())).forEach(userInfo -> {
            userInfo.addState(BitStateUtil.OP_BIND_PHONE);
            userInfoMapper.updateByPrimaryKey(userInfo);
        });
    }
    @Test
    public void testdD() throws Exception {
        List<LoginInfo> loginInfos = loginInfoMapper.query(2);
        List<String> tokens = loginInfos.stream().map(LoginInfo::getCreateantToken).collect(Collectors.toList());
        Map<String, Long> map = CreateantRequestUtil.queryUserIdByToken(tokens);
        map.forEach(loginInfoMapper::updateUserCreateantUid);
    }
    @Test
    public void testDDG() throws Exception {
        CreateantRequestUtil.queryUserIdByToken(Collections.singleton("SExKUU9pMTdMZVB4Q2I5SW9scEtFbFpIQmtYQ0pQYzBWUXRnZ2trZjNzTT0="));
    }


}
