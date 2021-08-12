package com.tanghaichao.crm.workbench.dao;

import com.tanghaichao.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getByName(String company);

    int save(Customer cus);
}
