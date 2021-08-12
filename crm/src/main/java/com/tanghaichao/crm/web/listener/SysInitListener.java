package com.tanghaichao.crm.web.listener;

import com.tanghaichao.crm.settings.domain.DicValue;
import com.tanghaichao.crm.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文作用域对象创建了");
        ServletContext application = event.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
        DicService ds = (DicService) ctx.getBean("dicService");
        Map<String, List<DicValue>> map = ds.getAll();

        for (String key: map.keySet()){
            application.setAttribute(key,map.get(key));
        }
        //application.setAttribute(key,数据字典);

        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e =  rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("上下文作用域销毁了");
    }
}
