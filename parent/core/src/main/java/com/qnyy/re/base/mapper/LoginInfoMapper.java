package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.LoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginInfoMapper {

    int insert(LoginInfo record);

    LoginInfo selectByPrimaryKey(Long uid);

    int updateByPrimaryKey(LoginInfo record);

    LoginInfo getByPhone(String phone);

    LoginInfo queryUserBySource(@Param("openId")String openId, @Param("source")Integer source);

    LoginInfo selectSystemAccount();

    int deleteUserAndCorrelation(Long uid);

    List<LoginInfo> query(Integer limit);

    void updateUserCreateantUid(@Param("token") String token, @Param("id") Long id);

}