package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getRemarkListByCid(String clueId);

    int delete(String clueId);
}
