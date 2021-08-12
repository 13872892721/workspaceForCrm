package com.tanghaichao.crm.workbench.web.controller;

import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.util.UUIDUtil;
import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.ActivityRemark;
import com.tanghaichao.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/users.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = userService.getUserList();
        return userList;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public Map<String,Object> save(Activity activity, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        String uuid = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setId(uuid);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        int res = activityService.saveActivity(activity);
        if (res == 1){
            map.put("success",true);
        }else {
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public Pagination<Activity> pageList(String pageNo, String pageSize, Activity activity){
        Map<String,Object> map = new HashMap<>();
        int skipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        map.put("skipCount",skipCount);
        map.put("pageSize",Integer.valueOf(pageSize));
        map.put("activity",activity);
        Pagination<Activity> vo = activityService.pageList(map);
        return vo;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public boolean delete(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        //Map<String,Object> map = new HashMap<>();
        boolean flag = activityService.deleteActivity(ids);
        return flag;
    }

    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        Map<String,Object> map = activityService.getActivity(id);

        return map;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public Map<String,Object> update(Activity activity, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        int res = activityService.updateActivity(activity);
        if (res == 1){
            map.put("success",true);
        }else {
            map.put("success",false);
        }
        return map;
    }

    @RequestMapping("/detail.do")
    public ModelAndView detail(String id){
        ModelAndView md = new ModelAndView();
        Activity activity = activityService.detail(id);
        md.addObject("a",activity);
        md.setViewName("/workbench/activity/detail.jsp");
        return md;
    }

    @RequestMapping("/getRemarkListByAid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);
        return arList;
    }

    @RequestMapping("/removeRemark.do")
    @ResponseBody
    public boolean removeRemark(String id){
        boolean flag = activityService.removeRemark(id);
        return flag;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(String noteContent,String activityId,HttpServletRequest request){
        boolean flag = true;
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        User user = (User) request.getSession().getAttribute("user");
        activityRemark.setCreateBy(user.getName());
        activityRemark.setEditFlag("0");
        activityRemark.setActivityId(activityId);
        int count = activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        if (count ==0){
            flag = false;
            map.put("success",flag);
        }
        map.put("success",flag);
        map.put("ar",activityRemark);

        return map;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(ActivityRemark ar,HttpServletRequest request){
        ar.setEditTime(DateTimeUtil.getSysTime());
        User user = (User) request.getSession().getAttribute("user");
        ar.setEditBy(user.getName());
        ar.setEditFlag("1");

        boolean flag = activityService.updateRemark(ar);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        return map;
    }
}
