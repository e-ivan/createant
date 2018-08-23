package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserToken;

public interface UserTokenMapper {

    int insert(UserToken record);

    UserToken selectByPrimaryKey(Long uid);

    UserToken selectByToken(String token);

    int updateByPrimaryKey(UserToken record);
}