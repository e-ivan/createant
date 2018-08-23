package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.UserCashAccount;
import java.util.List;

public interface UserCashAccountMapper {

    int insert(UserCashAccount record);

    UserCashAccount selectByPrimaryKey(Long id);

    List<UserCashAccount> selectAll();

    int updateByPrimaryKey(UserCashAccount record);

    UserCashAccount selectByUserId(Long uid);
}