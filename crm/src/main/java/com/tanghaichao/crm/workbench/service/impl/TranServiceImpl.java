package com.tanghaichao.crm.workbench.service.impl;

import com.tanghaichao.crm.util.UUIDUtil;
import com.tanghaichao.crm.workbench.dao.*;
import com.tanghaichao.crm.workbench.domain.*;
import com.tanghaichao.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;

    @Resource
    private TranHistoryDao tranHistoryDao;

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ContactsDao contactsDao;

    @Resource
    private CustomerDao customerDao;

    @Override
    public List<Tran> getTranList() {
        List<Tran> tranList = tranDao.getTranList();
        for (Tran tran :tranList){
            String customerId = tran.getCustomerId();
            String contactsId = tran.getContactsId();
            if (customerId!=null && !"".equals(customerId)){
                String company = tranDao.getCustomerNameByCid(customerId);
                tran.setCustomerId(company);
            }
            if (contactsId!=null && !"".equals(contactsId)){
                String fullname = tranDao.getContactsNameByCid(contactsId);
                tran.setContactsId(fullname);
            }
        }
        return tranList;
    }

    @Override
    public List<String> getCustomerName(String name) {
        List<String> nameList = tranDao.getCustomerName(name);
        return nameList;
    }

    @Override
    public List<Activity> getActivityListByName(String name) {
        return activityDao.getActivityList(name);
    }

    @Override
    public List<Contacts> getContactsListByName(String name) {
        return contactsDao.getContactsListByName(name);
    }

    @Override
    public boolean save(Tran tran,String company) {
        boolean flag = true;
        Customer customer = customerDao.getByName(company);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(company);
            customer.setOwner(tran.getOwner());
            customer.setCreateTime(tran.getCreateTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setContactSummary(tran.getContactSummary());
            int count = customerDao.save(customer);
            if (count == 0){
                flag = false;
            }
        }
        tran.setId(UUIDUtil.getUUID());
        tran.setCustomerId(customer.getId());
        int count2 = tranDao.save(tran);
        if (count2 == 0){
            flag =false;
        }
        return flag;
    }

    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.getTranById(id);
        String customerId = tran.getCustomerId();
        String contactsId = tran.getContactsId();
        if (customerId!=null && !"".equals(customerId)){
            String company = tranDao.getCustomerNameByCid(customerId);
            tran.setCustomerId(company);
        }
        if (contactsId!=null && !"".equals(contactsId)){
            String fullname = tranDao.getContactsNameByCid(contactsId);
            tran.setContactsId(fullname);
        }
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryListByTranId(String tranId) {

        return tranHistoryDao.getTranHistoryListByTranId(tranId);
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count1 = tranDao.changeStage(tran);
        if (count1 == 0){
            flag = false;
        }
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setMoney(tran.getMoney());
        th.setStage(tran.getStage());
        th.setTranId(tran.getId());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(tran.getEditTime());
        int count2 = tranHistoryDao.save(th);
        if (count2 == 0){
            flag = false;
        }
        return flag;
    }
}
