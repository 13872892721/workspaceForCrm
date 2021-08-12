package com.tanghaichao.crm.workbench.service;

import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.Clue;
import com.tanghaichao.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    Pagination<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    List<Activity> showActivityList(String clueId);

    boolean deleteRelation(String clueId, String activityId);

    List<Activity> getActivityNotRelationList(String clueId,String name);

    boolean bundActivity(String cid,String[] aids);

    List<Activity> getActivityList(String aname);

    boolean convert(String clueId, String createBy, String createTime, Tran t);
}
