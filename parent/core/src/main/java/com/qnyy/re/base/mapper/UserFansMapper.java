package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserFans;
import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.query.UserFansQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFansMapper {
    int insert(@Param("uid") Long uid, @Param("fan") Long fan);

    UserFans selectByUserAndFan(@Param("uid") Long uid, @Param("fan") Long fan);

    List<UserInfo> query(UserFansQueryObject qo);

    int queryCount(UserFansQueryObject qo);

    int updateByPrimaryKey(UserFans record);
}