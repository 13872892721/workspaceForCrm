package com.tanghaichao.crm;

import com.tanghaichao.crm.settings.service.DicService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.util.MD5Util;
import com.tanghaichao.crm.web.listener.SysInitListener;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest01 {

    /**
     * 验证失效时间
     */
    @Test
    public void test01(){
        String expireTime = "2020-01-01 01:01:01";
        //当前系统时间
        String currentDate = DateTimeUtil.getSysTime();
        int res = expireTime.compareTo(currentDate);
        if (res > 0){
            System.out.println("验证通过");
        }else {
            System.out.println("验证失败");
        }
    }

    /**
     * 验证锁定状态
     */
    @Test
    public void test02(){

        String lockState = "1";
        if ("0".equals(lockState)){
            System.out.println("用户已锁定");
        }else {
            System.out.println("验证通过");
        }
    }

    /**
     * 验证IP地址
     */
    @Test
    public void test03(){

        //浏览器端的IP地址
        String ip = "192.168.0.3";
        //允许访问的IP地址群
        String alLowIps = "192.168.0.1,192.168.0.2";
        if (alLowIps.contains(ip)){
            System.out.println("有效的ip,允许访问");
        }else {
            System.out.println("ip已失效，请联系管理员");
        }
    }

    @Test
    public void test04(){
        String pwd = "123";
        String md5Pwd = MD5Util.getMD5(pwd);
        System.out.println(md5Pwd);
    }

}
