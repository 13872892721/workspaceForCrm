package com.tanghaichao.crm.workbench.service.impl;

import com.tanghaichao.crm.settings.domain.User;
import com.tanghaichao.crm.settings.service.UserService;
import com.tanghaichao.crm.util.DateTimeUtil;
import com.tanghaichao.crm.util.UUIDUtil;
import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.dao.ActivityDao;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.ActivityRemark;
import com.tanghaichao.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import com.tanghaichao.crm.workbench.dao.ActivityRemarkDao;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Resource
    private UserService userService;

    @Override
    public int saveActivity(Activity activity) {
        int res = activityDao.saveActivity(activity);
        return res;
    }

    @Override
    public Pagination<Activity> pageList(Map<String,Object> map) {
        Pagination<Activity> vo = new Pagination<>();
        List<Activity> activityList = activityDao.getActivityListByCondition(map);
        int count = activityDao.getTotalByCondition(map);
        vo.setDataList(activityList);
        vo.setTotal(count);
        return vo;
    }


    @Override
    public boolean deleteActivity(String[] ids) {
        boolean flag = true;
        //查询要删除的市场活动数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除市场活动，返回实际影响的条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        if (count1 != count2){
            flag = false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getActivity(String id) {
        Map<String,Object> map = new HashMap<>();
        List<User> userList = userService.getUserList();
        Activity activity = activityDao.getActivityById(id);
        map.put("uList",userList);
        map.put("a",activity);
        return map;
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityDao.update(activity);
    }

    @Override
    public Activity detail(String id) {
        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        return activityRemarkDao.getRemarkListByAid(activityId);
    }

    @Override
    public boolean removeRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.removeRemark(id);
        if (count ==0){
            flag = false;
        }
        return flag;
    }

    @Override
    public int saveRemark(ActivityRemark activityRemark) {


        int count = activityRemarkDao.saveRemark(activityRemark);
        return count;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count ==0 ){
            flag = false;
        }
        return flag;
    }
}
