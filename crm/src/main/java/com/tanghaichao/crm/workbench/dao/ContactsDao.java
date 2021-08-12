package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);

    List<Contacts> getContactsListByName(String name);
}
