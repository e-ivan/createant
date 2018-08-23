package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.ReVoteUser;
import org.apache.ibatis.annotations.Param;

public interface ReVoteUserMapper {

    int insert(@Param("uid") Long uid, @Param("reId") Long reId);

    ReVoteUser selectByUserAndRe(@Param("uid") Long uid, @Param("reId") Long reId);

    int updateByPrimaryKey(ReVoteUser record);
}