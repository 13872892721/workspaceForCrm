package com.tanghaichao.crm.workbench.service;

import com.tanghaichao.crm.workbench.domain.Activity;
import com.tanghaichao.crm.workbench.domain.Contacts;
import com.tanghaichao.crm.workbench.domain.Tran;
import com.tanghaichao.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    List<Tran> getTranList();

    List<String> getCustomerName(String name);

    List<Activity> getActivityListByName(String name);

    List<Contacts> getContactsListByName(String name);

    boolean save(Tran tran,String company);

    Tran getTranById(String id);

    List<TranHistory> getTranHistoryListByTranId(String tranId);

    boolean changeStage(Tran tran);
}
