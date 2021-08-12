package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getActivityListByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
