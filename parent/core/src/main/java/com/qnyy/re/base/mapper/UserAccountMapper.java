package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserAccount;

public interface UserAccountMapper {

    int insert(Long uid);

    UserAccount selectByPrimaryKey(Long uid);

    int updateByPrimaryKey(UserAccount record);
}