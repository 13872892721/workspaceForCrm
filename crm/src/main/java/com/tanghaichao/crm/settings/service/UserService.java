package com.tanghaichao.crm.settings.service;

import com.tanghaichao.crm.exception.LoginException;
import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.domain.Activity;

import java.util.List;

public interface UserService {

    User loginUser(String loginAct,String loginPwd,String ip) throws LoginException;

    List<User> getUserList();
}
