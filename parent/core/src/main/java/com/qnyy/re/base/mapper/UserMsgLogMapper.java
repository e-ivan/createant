package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserMsgLog;
import com.qnyy.re.base.query.UserMsgLogQueryObject;

import java.util.List;

public interface UserMsgLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserMsgLog record);

    UserMsgLog selectByPrimaryKey(Long id);

    List<UserMsgLog> selectAll();

    List<UserMsgLog> query(UserMsgLogQueryObject qo);
    int queryCount(UserMsgLogQueryObject qo);

    int updateByPrimaryKey(UserMsgLog record);
}