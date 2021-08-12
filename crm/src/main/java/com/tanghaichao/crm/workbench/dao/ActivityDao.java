package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int saveActivity(Activity activity);

    List<Activity> getActivityListByCondition(Map<String,Object> map);

    int getTotalByCondition(Map<String,Object> map);


    int delete(String[] ids);

    Activity getActivityById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByAid(List<ClueActivityRelation> carList);

    List<Activity> getActivityNotRelationList(@Param("carList") List<ClueActivityRelation> carList,@Param("name") String name);

    List<Activity> getActivityList(String aname);
}
