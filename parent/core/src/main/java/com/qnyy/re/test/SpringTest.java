package com.qnyy.re.test;

import com.qnyy.re.base.mapper.UserFansMapper;
import com.qnyy.re.base.mapper.UserInfoMapper;
import com.qnyy.re.base.mapper.UserStatisticsMapper;
import com.qnyy.re.base.mapper.UserVisitHistoryMapper;
import com.qnyy.re.base.service.ISysBroadcastService;
import com.qnyy.re.base.service.IUserAccountService;
import com.qnyy.re.base.util.AmapUtil;
import com.qnyy.re.base.util.SubmailSmsUtil;
import com.qnyy.re.base.vo.AddressComponentVO;
import com.qnyy.re.business.mapper.BaseOrderMapper;
import com.qnyy.re.business.mapper.ReItemMapper;
import com.qnyy.re.business.mapper.ReMomentMapper;
import com.qnyy.re.business.mapper.ReOrderMapper;
import com.qnyy.re.business.service.IBaseOrderService;
import com.qnyy.re.business.service.ICashService;
import com.qnyy.re.business.service.IReMomentService;
import com.qnyy.re.business.service.IUserClassRelationService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;


/**
 * Created by hh on 2017.8.22 0022.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
@Log4j
public class SpringTest {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    @Autowired
    private IBaseOrderService baseOrderService;
    @Autowired
    private ReMomentMapper mapper;
    @Autowired
    private ReItemMapper itemMapper;
    @Autowired
    private BaseOrderMapper orderMapper;
    @Autowired
    private UserStatisticsMapper userStatisticsMapper;

    @Autowired
    private ReOrderMapper reOrderMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserFansMapper userFansMapper;
    @Autowired
    private UserVisitHistoryMapper historyMapper;
    @Autowired
    private ICashService cashService;
    @Autowired
    private IReMomentService reMomentService;
    @Autowired
    private IUserAccountService userAccountService;

    @Autowired
    private IUserClassRelationService userClassRelationService;
    @Autowired
    private ISysBroadcastService sysBroadcastService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSSS() throws Exception {
    }

    @Test
    public void testDDD() throws Exception {
        AddressComponentVO addressByLocation = AmapUtil.getAddressByLocation("103.86521388372", "30.791939841354");
        System.out.println(addressByLocation);
    }

    @Test
    public void testdfd() throws Exception {
        System.out.println(SubmailSmsUtil.sendVerifyCode("112", "111111111111"));
    }

    @Test
    public void testFGDFG() throws Exception {
        System.out.println(AmapUtil.getAddressByLocation("12.30", "320.1"));
    }
    @Test
    public void testSD() throws Exception {
        System.out.println(NumberUtils.toInt("[]"));
    }

}
