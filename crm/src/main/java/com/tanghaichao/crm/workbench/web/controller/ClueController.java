package com.tanghaichao.crm.workbench.web.controller;

import com.tanghaichao.crm.settings.dao.UserDao;
import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.util.UUIDUtil;
import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.Clue;
import com.tanghaichao.crm.workbench.domain.Tran;
import com.tanghaichao.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource
    private UserService userService;
    @Resource
    private ClueService clueService;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        return userService.getUserList();
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public boolean save(Clue clue, HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        return clueService.save(clue);
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public Pagination<Clue> pageList(Clue clue, Integer pageNo, Integer pageSize){
        Integer skipCount = (pageNo - 1)* pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("clue",clue);
        Pagination<Clue> vo = clueService.pageList(map);
        return  vo;
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        Clue clue = clueService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;
    }

    @RequestMapping("/showActivityList.do")
    @ResponseBody
    public List<Activity> showActivityList(String clueId){
        return clueService.showActivityList(clueId);
    }

    @RequestMapping("/deleteRelation.do")
    @ResponseBody
    public boolean deleteRelation(String clueId,String activityId){
        return clueService.deleteRelation(clueId,activityId);
    }

    @RequestMapping("/getActivityNotRelationList.do")
    @ResponseBody
    public List<Activity> getActivityNotRelationList(String clueId,String name){
        return clueService.getActivityNotRelationList(clueId,name);
    }

    @RequestMapping("/bundActivity.do")
    @ResponseBody
    public boolean bundActivity(String cid,String[] aid){
        return clueService.bundActivity(cid,aid);
    }

    @RequestMapping("/getActivityList.do")
    @ResponseBody
    public List<Activity> getActivityList(String aname){
        return clueService.getActivityList(aname);
    }

    @RequestMapping("/convert.do")
    public String convert(HttpServletRequest request){
        String clueId = request.getParameter("clueId");
        User user = (User)request.getSession().getAttribute("user");
        String createBy = user.getName();
        String createTime = DateTimeUtil.getSysTime();
        Tran t = null;
        String activityId = (request.getParameter("activityId"));
        if (activityId != null){
            t = new Tran();
            t.setId(UUIDUtil.getUUID());
            t.setMoney(request.getParameter("money"));
            t.setName(request.getParameter("name"));
            t.setExpectedDate(request.getParameter("expectedDate"));
            t.setStage(request.getParameter("stage"));
            t.setActivityId(request.getParameter("activityId"));
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }

        boolean flag = clueService.convert(clueId,createBy,createTime,t);
        if (flag == true){
            return "redirect:/workbench/clue/index.jsp";
        }else {
            return "redirect:/workbench/clue/convert.jsp";
        }

    }
}
