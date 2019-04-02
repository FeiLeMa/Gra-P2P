package com.alag.p2p.business.module.user.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.tools.ObjToOne;
import com.alag.p2p.business.module.user.server.mapper.UserMapper;
import com.alag.p2p.business.module.user.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;

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
        return objV==null?allUserCount:ObjToOne.objToLong(objV);
    }
}
