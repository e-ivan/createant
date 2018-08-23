package com.qnyy.re.base.service;

import com.qnyy.re.base.entity.UserToken;

/**
 * 用户令牌服务
 * Created by E_Iva on 2017/11/24.
 */
public interface IUserTokenService {
    /**
     * 保存
     */
    UserToken save(Long uid,String rongYunToken);

    /**
     * 获取令牌
     */
    UserToken getTokenByUid(String uid);
    UserToken getTokenByUid(Long uid);

    UserToken getByToken(String token);

    /**
     * 更新token
     */
    UserToken updateToken(Long uid);

    /**
     * 更新融云token
     */
    UserToken updateRongYunToken(Long uid, String rongYunToken);
}
