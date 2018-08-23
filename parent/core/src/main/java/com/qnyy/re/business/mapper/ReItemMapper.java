package com.qnyy.re.business.mapper;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.business.entity.ReItem;
import com.qnyy.re.business.query.ReItemQueryObject;

import java.util.List;

public interface ReItemMapper {

    int insert(ReItem record);

    /**
     * 批量插入
     */
    int insertBatch(List<ReItem> list);

    int updateByPrimaryKey(ReItem record);

    List<UserInfo> select2User(Long id);

    List<ReItem> query(ReItemQueryObject qo);

    int queryCount(ReItemQueryObject qo);

    /**
     * 根据红包、用户、状态获取相应对象
     */
    ReItem selectOnUserAndRe(ReItemQueryObject qo);
}