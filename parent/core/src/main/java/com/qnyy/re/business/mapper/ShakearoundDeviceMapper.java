package com.qnyy.re.business.mapper;

import com.qnyy.re.business.entity.ShakearoundDevice;
import com.qnyy.re.business.query.ShakearoundDeviceQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface ShakearoundDeviceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShakearoundDevice record);

    /**
     * 批量插入
     */
    int insertBatch(List<ShakearoundDevice> list);

    ShakearoundDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKey(ShakearoundDevice record);

    List<ShakearoundDevice> query(ShakearoundDeviceQueryObject qo);

    int queryCount(ShakearoundDeviceQueryObject qo);

    ShakearoundDevice selectByIBeaconId(String iBeaconId);
    ShakearoundDevice selectByDeviceId(Integer deviceId);

    List<ShakearoundDevice> queryByUid(@Param("uid") Long uid, @Param("storeId") Long storeId);

    List<Long> queryStoreIdByUid(Long uid);

    Long getDeviceReMomentId(@Param("major") Integer major, @Param("minor") Integer minor, @Param("state") Integer state);

    List<ShakearoundDevice> selectBatch(Collection<Long> ids);

    void deleteBatch(Collection<Long> ids);
}