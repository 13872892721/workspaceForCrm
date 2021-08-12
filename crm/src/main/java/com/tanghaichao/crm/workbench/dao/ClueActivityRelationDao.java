package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> getClueActivityRelationByClueID(String clueId);

    int deleteRelation(@Param("clueId") String clueId, @Param("activityId") String activityId);

    int bundActivity(List<ClueActivityRelation> carList);

    int delete(String clueId);
}
