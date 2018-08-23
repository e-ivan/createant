package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.UserMsgLog;
import com.qnyy.re.base.enums.UserMsgLogTypeEnum;
import com.qnyy.re.base.mapper.UserMsgLogMapper;
import com.qnyy.re.base.query.UserMsgLogQueryObject;
import com.qnyy.re.base.service.IUserMsgLogService;
import com.qnyy.re.base.util.container.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by E_Iva on 2017.12.13.0013.
 */
@Service
public class UserMsgLogServiceImpl implements IUserMsgLogService {

    @Autowired
    private UserMsgLogMapper mapper;

    @Override
    public void save(Long from, Long to, UserMsgLogTypeEnum type, Long objectId, String content) {
        //不是自己操作自己时
        if (!from.equals(to)) {
            UserMsgLog log = new UserMsgLog(from, to, type.getCode(), objectId, content);
            mapper.insert(log);
        }
    }

    @Override
    public void save(Long from, Long to, UserMsgLogTypeEnum type, Long objectId) {
        this.save(from, to, type, objectId,"");
    }

    @Override
    public PageResult<UserMsgLog> queryUserMsgLog(UserMsgLogQueryObject qo) {
        int count = mapper.queryCount(qo);
        List<UserMsgLog> list = mapper.query(qo);
        return new PageResult<>(list,count,qo.getCurrentPage(),qo.getPageSize());
    }
}
