package com.qnyy.re.base.mapper;

import com.qnyy.re.base.entity.ApiInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiInfoMapper {

    void batchDelete(List<Long> ids);

    int batchInsert(List<ApiInfo> list);

    int batchUpdate(List<ApiInfo> list);

    ApiInfo selectByPrimaryKey(Long id);

    ApiInfo selectByUri(@Param("uri") String uri, @Param("site") String site);

    List<ApiInfo> selectAllBySite(String site);

    int updateByUri(ApiInfo record);
}