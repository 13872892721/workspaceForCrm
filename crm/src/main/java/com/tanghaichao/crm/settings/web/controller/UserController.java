package com.tanghaichao.crm.settings.web.controller;

import com.tanghaichao.crm.exception.LoginException;
import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.settings.service.impl.UserServiceImpl;
import com.tanghaichao.crm.util.IpAddressUtil;
import com.tanghaichao.crm.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/setting/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> login(String loginAct,String loginPwd,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        //接收浏览器的ip地址
        String ip = IpAddressUtil.getIpAddress(request);
        try{
            User user = userService.loginUser(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            map.put("success",true);

        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }
}
