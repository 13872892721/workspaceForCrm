package com.tanghaichao.crm.workbench.service;

import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveActivity(Activity activity);

    Pagination<Activity> pageList(Map<String,Object> map);

    boolean deleteActivity(String[] ids);

    Map<String,Object> getActivity(String id);

    int updateActivity(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean removeRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark ar);
}
