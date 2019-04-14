package com.alag.p2p.business.module.user.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.core.common.tools.MD5;
import com.alag.p2p.business.core.common.tools.ObjToOne;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.api.model.User;
import com.alag.p2p.business.module.user.server.mapper.FinanceAccountMapper;
import com.alag.p2p.business.module.user.server.mapper.UserMapper;
import com.alag.p2p.business.module.user.server.service.UserService;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Long getAUserCount() {

        //获取指定key的value值
        Object objV = redisService.get(Constants.ALL_USER_COUNT);
        Long allUserCount = null;
        //判断是否有值
        if (null == objV) {

            //去数据库查询
            allUserCount = userMapper.selectAllUserCount();

            //将该值存放到redis缓存中
            redisService.set(Constants.ALL_USER_COUNT, allUserCount, 15L);
        }
        return objV == null ? allUserCount : ObjToOne.objToLong(objV);
    }

    @Override
    public User getUserByPhone(String phone) {
        User user = userMapper.selectUserByPhone(phone);
        return user;
    }

    @Transactional
    @Override
    public ServerResponse register(String phone, String passwd) {

        //新增用户
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(MD5.get(passwd));
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        User userInfo = userMapper.selectUserByPhone(phone);
        if (userInfo != null){
            return ServerResponse.createByErrorMessage("发生未知错误，这个号码已被抢先注册了...");
        }
        int insertUserCount = userMapper.insertSelective(user);

        if (insertUserCount > 0) {
            userInfo = userMapper.selectUserByPhone(phone);
            //新增帐户
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(userInfo.getId());
            financeAccount.setAvailableMoney(888.0);
            int insertFinanceCount = financeAccountMapper.insertSelective(financeAccount);
            if (insertFinanceCount < 0) {
                return ServerResponse.createByErrorMessage("注册失败");
            }

        } else {
            return ServerResponse.createByErrorMessage("注册失败");
        }


        return ServerResponse.createBySuccess(userInfo);
    }

    @Override
    public int updateInfo(User userSession) {
        return userMapper.updateByPrimaryKey(userSession);
    }

    @Override
    public FinanceAccount getFinanceAccountById(Integer id) {
        return financeAccountMapper.selectFinanceAccountByUid(id);
    }

    @Override
    public User login(String phone, String loginPassword) {
        //1.根据用户手机号和登录密码查询用户信息
        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,MD5.get(loginPassword));

        //判断用户是否存在
        if (null != user) {

            //2.更新用户的登录时间
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);

        }

        return user;
    }

    @TxcTransaction(propagation = DTXPropagation.SUPPORTS)
    @Override
    public ServerResponse updateFAById(Map<String, Object> paramMap) {
        int mRet = financeAccountMapper.updateFinanceAccountByBid(paramMap);
        if (mRet > 0 ){
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }



    @TxcTransaction(propagation = DTXPropagation.SUPPORTS)
    @Override
    public ServerResponse updateFAByIncomeBack(Map<String, Object> paramMap) {
        int mRet = financeAccountMapper.updateFinanceAccountByIncomeBack(paramMap);
        if (mRet > 0 ){
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }


}
