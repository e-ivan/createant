package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.query.UserStatisticsQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserStatisticsMapper {
    int insert(Long uid);

    UserStatistics selectByPrimaryKey(Long uid);

    int update(UserStatistics userStatistics);

    int addCount(@Param("uid") Long uid, @Param("field") String field);

    List<UserInfo> query(UserStatisticsQueryObject qo);
    int queryCount(UserStatisticsQueryObject qo);
}