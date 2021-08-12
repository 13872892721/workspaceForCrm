package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.Tran;

import java.util.List;

public interface TranDao {

    int save(Tran t);

    List<Tran> getTranList();

    List<String> getCustomerName(String name);

    String getCustomerNameByCid(String customerId);

    String getContactsNameByCid(String contactsId);

    Tran getTranById(String id);

    int changeStage(Tran tran);
}
