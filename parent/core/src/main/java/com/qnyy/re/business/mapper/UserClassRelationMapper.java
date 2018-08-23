package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.UserClassRelation;
import com.qnyy.re.business.query.UserClassRelationQueryObject;

import java.util.List;

public interface UserClassRelationMapper {

    int insert(UserClassRelation record);

    int queryUpperCount(Long uid);

    UserClassRelation selectByJunior(Long uid);

    List<UserClassRelation> query(UserClassRelationQueryObject qo);
    int queryCount(UserClassRelationQueryObject qo);

    List<UserClassRelation> selectAll();

    int updateByPrimaryKey(UserClassRelation record);
}