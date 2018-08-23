package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserVisitHistory;
import com.qnyy.re.base.query.UserVisitHistoryQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserVisitHistoryMapper {

    int insert(@Param("uid") Long uid, @Param("visitor") Long visitor);

    UserVisitHistory selectByUserAndVisitor(@Param("uid") Long uid, @Param("visitor") Long visitor);

    int addTime(UserVisitHistory record);

    List<UserInfo> query(UserVisitHistoryQueryObject qo);

    int queryCount(UserVisitHistoryQueryObject qo);

}