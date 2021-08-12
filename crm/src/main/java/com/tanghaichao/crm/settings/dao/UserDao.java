package com.tanghaichao.crm.settings.dao;

import com.tanghaichao.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    User login(@Param(value = "loginAct") String loginAct,@Param("loginPwd") String loginPwd);

    List<User> getUserList();
}
