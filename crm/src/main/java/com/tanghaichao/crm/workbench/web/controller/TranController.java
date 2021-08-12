package com.tanghaichao.crm.workbench.web.controller;

import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.Contacts;
import com.tanghaichao.crm.workbench.domain.Tran;
import com.tanghaichao.crm.workbench.domain.TranHistory;
import com.tanghaichao.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

    @Resource
    private TranService tranService;

    @Resource
    private UserService userService;

    @RequestMapping("/tran.do")
    @ResponseBody
    public List<Tran> getTranList(String id){
        if (id != null && "".equals(id)){

        }
        return tranService.getTranList();
    }

    @RequestMapping("/add.do")
    public ModelAndView getUserList(){
        List<User> uList = userService.getUserList();
        ModelAndView mav = new ModelAndView();
        mav.addObject("uList",uList);
        mav.setViewName("/workbench/transaction/save.jsp");
        return mav;
    }

    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        return tranService.getCustomerName(name);
    }

    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String name){
        return tranService.getActivityListByName(name);
    }

    @RequestMapping("/getContactsListByName.do")
    @ResponseBody
    public List<Contacts> getContactsListByName(String name){
        return tranService.getContactsListByName(name);
    }

    @RequestMapping("/save.do")
    public String save(Tran tran, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        tran.setCreateBy(createBy);
        tran.setCreateTime(DateTimeUtil.getSysTime());
        String company = request.getParameter("company");
        boolean flag = tranService.save(tran,company);
        return "/workbench/transaction/index.jsp";
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id,HttpServletRequest request){

        Tran tran = tranService.getTranById(id);
        ModelAndView mad = new ModelAndView();
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        Set<String> keys = pMap.keySet();
        for (String key:keys){
            if (key.equals(tran.getStage())){
                mad.addObject("possibility",pMap.get(key));
            }
        }
        mad.addObject("tran",tran);
        mad.setViewName("/workbench/transaction/detail.jsp");
        return mad;
    }

    @RequestMapping("/getTranHistoryListByTranId.do")
    @ResponseBody
    public List<TranHistory> getTranHistoryListByTranId(String tranId,HttpServletRequest request){
        List<TranHistory> thList = tranService.getTranHistoryListByTranId(tranId);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        for (TranHistory th:thList){
            String possibility = pMap.get(th.getStage());
            th.setPossibility(possibility);
        }
        return thList;
    }

    @RequestMapping("/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(Tran tran,HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editTime = DateTimeUtil.getSysTime();
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        boolean flag = tranService.changeStage(tran);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        tran.setPossibility(pMap.get(tran.getStage()));
        map.put("success",flag);
        map.put("t",tran);
        return map;
    }
}
