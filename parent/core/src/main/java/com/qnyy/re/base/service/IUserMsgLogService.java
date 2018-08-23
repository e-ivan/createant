package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.UserMsgLog;
import com.qnyy.re.base.enums.UserMsgLogTypeEnum;
import com.qnyy.re.base.query.UserMsgLogQueryObject;
import com.qnyy.re.base.util.container.PageResult;

/**
 * 用户消息日志服务
 * Created by E_Iva on 2017.12.13.0013.
 */
public interface IUserMsgLogService {
    void save(Long from, Long to, UserMsgLogTypeEnum type, Long objectId, String content);
    void save(Long from, Long to, UserMsgLogTypeEnum type, Long objectId);
    PageResult<UserMsgLog> queryUserMsgLog(UserMsgLogQueryObject qo);
}
