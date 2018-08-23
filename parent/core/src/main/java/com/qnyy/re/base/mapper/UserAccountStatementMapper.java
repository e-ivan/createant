package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserAccountStatement;
import com.qnyy.re.business.query.UserAccountStatementQueryObject;

import java.util.List;

public interface UserAccountStatementMapper {

    int insert(UserAccountStatement record);

    int queryCount(UserAccountStatementQueryObject qo);

    List<UserAccountStatement> query(UserAccountStatementQueryObject qo);
}