package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.query.UserInfoQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserInfoMapper {

    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Long uid);

    int updateByPrimaryKey(UserInfo record);

    void updateUserLocation(@Param("uid") Long uid, @Param("lng") String lng, @Param("lat") String lat);

    UserInfo selectByPromoCode(String promoCode);

    int queryCount(UserInfoQueryObject qo);

    List<UserInfo> query(UserInfoQueryObject qo);

}