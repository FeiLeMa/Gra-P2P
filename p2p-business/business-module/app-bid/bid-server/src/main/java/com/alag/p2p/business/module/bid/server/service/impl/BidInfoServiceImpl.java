package com.alag.p2p.business.module.bid.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.tools.ObjToOne;
import com.alag.p2p.business.module.bid.server.mapper.BidInfoMapper;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public Double getAllBidMoney() {//获取指定key的操作对象

        //获取指定key的value值
        Object objV = redisService.get(Constants.ALL_BID_MONEY);
        Double allBidMoney = null;
        //判断是否有值
        if (null == objV) {
            //去数据库查询
            allBidMoney = bidInfoMapper.selectAllBidMoney();

            //存放到redis缓存中
            redisService.set(Constants.ALL_BID_MONEY, allBidMoney, 15L);
        }

        return objV==null?allBidMoney:ObjToOne.objToDouble(objV);
    }
}
