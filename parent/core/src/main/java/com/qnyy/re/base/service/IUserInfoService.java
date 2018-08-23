package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.UserInfo;
import com.qnyy.re.base.entity.UserStatistics;
import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.query.UserFansQueryObject;
import com.qnyy.re.base.query.UserInfoQueryObject;
import com.qnyy.re.base.query.UserStatisticsQueryObject;
import com.qnyy.re.base.query.UserVisitHistoryQueryObject;
import com.qnyy.re.base.util.container.MgrPageResult;
import com.qnyy.re.base.util.container.PageResult;
import com.qnyy.re.base.vo.param.UpdateUserInfoVO;

import java.util.List;

/**
 * 用户信息服务
 * Created by E_Iva on 2017/11/24.
 */
public interface IUserInfoService {
    /**
     * 保存用户信息
     */
    UserToken save(Long uid, Long createantUid, String headUrl, String nickname, Integer sex, String lat, String lng, Long[] bitStates);

    /**
     * 获取用户信息
     */
    UserInfo getByUid(Long uid);

    /**
     * 更新用户信息
     */
    void update(UserInfo userInfo);

    /**
     * 更新用户地理位置
     * @param lng   经度
     * @param lat   纬度
     */
    void updateLocation(Long uid,String lng,String lat);

    /**
     * 更新用户基本信息
     */

      UserInfo updateUserInfo(UpdateUserInfoVO vo);

    /**
     * 获取用户统计信息
     */
    UserStatistics getStatisticsByUid(Long uid);

    /**
     * 更新统计信息
     */
    void updateUserStatistics(UserStatistics userStatistics);

    /**
     * 统计信息自增
     */
    void addUserStatisticsCount(Long uid,UserStatistics.CountField field);

    /**
     * 更具统计信息查询用户
     */
    List<UserInfo> queryUserByStatistics(UserStatisticsQueryObject qo);

    /**
     * 添加用户访问历史
     * @param uid   被访用户
     */
    void addUserHistory(Long uid);

    /**
     * 用户访问记录
     * @param qo
     */
    PageResult<UserInfo> queryUserVisitHistory(UserVisitHistoryQueryObject qo);

    /**
     * 操作关注
     * @param uid 关注用户
     * @param add true 关注  false 取消
     */
    void handleUserFans(Long uid, boolean add);

    /**
     * 检查是否关注
     */
    boolean checkIfUserFan(Long uid);

    /**
     * 查询用户关注与被关注
     */
    PageResult<UserInfo> queryUserFans(UserFansQueryObject qo);

    UserInfo selectUserByPromoCode(String promoCode);

    int queryUserCount();

    /**
     * 查询用户
     */
    MgrPageResult<UserInfo> queryUser(UserInfoQueryObject qo);

    /**
     * 更新用户类型
     */
    void updateUserType(Long uid, String type);
}
