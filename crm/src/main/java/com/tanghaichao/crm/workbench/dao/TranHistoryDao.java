package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getTranHistoryListByTranId(String tranId);
}
