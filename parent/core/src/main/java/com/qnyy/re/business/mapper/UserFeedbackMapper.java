package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.UserFeedback;

import java.util.List;

public interface UserFeedbackMapper {

    int insert(UserFeedback record);

    UserFeedback selectByPrimaryKey(Long id);

    UserFeedback selectByUserAtLatest(Long uid);

    List<UserFeedback> selectAll();

    int updateByPrimaryKey(UserFeedback record);
}