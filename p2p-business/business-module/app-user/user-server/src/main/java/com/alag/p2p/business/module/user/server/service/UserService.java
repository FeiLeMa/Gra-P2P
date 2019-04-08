package com.alag.p2p.business.module.user.server.service;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.api.model.User;

public interface UserService {
    Long getAUserCount();

    int getUserByPhone(String phone);

    ServerResponse register(String phone, String passwd);

    int updateInfo(User userSession);

    FinanceAccount getFinanceAccountById(Integer id);

    User login(String phone, String loginPassword);
}
