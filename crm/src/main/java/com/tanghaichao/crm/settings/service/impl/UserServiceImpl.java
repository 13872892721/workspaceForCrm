package com.tanghaichao.crm.settings.service.impl;

import com.tanghaichao.crm.exception.LoginException;
import com.tanghaichao.crm.settings.dao.UserDao;
import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.util.MD5Util;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    public User loginUser(String loginAct, String loginPwd,String ip) throws LoginException {
        //将密码的明文形式转换为MD5形式
        String loginPwdMD5 = MD5Util.getMD5(loginPwd);
        User user = userDao.login(loginAct,loginPwdMD5);
        if (user == null){
            throw new LoginException("账号密码错误");
        }
        String currentTime = DateTimeUtil.getSysTime();
        if (currentTime.compareTo(user.getExpireTime())>0){
            throw new LoginException("账号已失效");
        }
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }
}
