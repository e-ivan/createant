package com.qnyy.re.base.service.impl;

import com.qnyy.re.base.entity.UserToken;
import com.qnyy.re.base.mapper.UserTokenMapper;
import com.qnyy.re.base.service.IUserTokenService;
import com.qnyy.re.base.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by E_Iva on 2017/11/24.
 */
@Service
public class UserTokenServiceImpl implements IUserTokenService {
    @Autowired
    private UserTokenMapper mapper;
    @Override
    public UserToken save(Long uid,String rongYunToken) {
        UserToken userToken = new UserToken();
        userToken.setUid(uid);
        userToken.setToken(MD5Util.encode(uid + new Date().toString()));
        userToken.setLoginTime(new Date());
        userToken.setUpdateTime(new Date());
        userToken.setRongYunToken(rongYunToken);
        mapper.insert(userToken);
        return userToken;
    }

    @Override
    public UserToken getTokenByUid(String uid) {
        try {
            Long id = Long.parseLong(uid);
            return this.getTokenByUid(id);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public UserToken getTokenByUid(Long uid) {
        return mapper.selectByPrimaryKey(uid);
    }

    @Override
    public UserToken getByToken(String token) {
        return mapper.selectByToken(token);
    }

    @Override
    public UserToken updateToken(Long uid) {
        UserToken userToken = mapper.selectByPrimaryKey(uid);
        userToken.setLoginTime(new Date());
        userToken.setToken(MD5Util.encode(String.format("%08d",uid) + new Date().getTime()));
        userToken.setPlatform(0);
        mapper.updateByPrimaryKey(userToken);
        return userToken;
    }

    @Override
    public UserToken updateRongYunToken(Long uid, String rongYunToken) {
        UserToken userToken = mapper.selectByPrimaryKey(uid);
        userToken.setRongYunToken(rongYunToken);
        mapper.updateByPrimaryKey(userToken);
        return userToken;
    }
}
