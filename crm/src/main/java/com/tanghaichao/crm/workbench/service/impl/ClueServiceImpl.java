package com.tanghaichao.crm.workbench.service.impl;

import com.tanghaichao.crm.util.UUIDUtil;
import com.tanghaichao.crm.vo.Pagination;
import com.tanghaichao.crm.workbench.dao.*;
import com.tanghaichao.crm.workbench.domain.*;
import com.tanghaichao.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    //线索相关的表
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;

    //市场活动相关的表
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;

    //客户相关的表
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;

    //联系人相关的表
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //交易相关的表
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count ==0 ){
            flag = false;
        }
        return flag;
    }

    @Override
    public Pagination<Clue> pageList(Map<String, Object> map) {
        Pagination<Clue> vo = new Pagination<>();
        int count = clueDao.getTotalByCondition(map);
        List<Clue> clueList = clueDao.getActivityListByCondition(map);
        vo.setTotal(count);
        vo.setDataList(clueList);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public List<Activity> showActivityList(String clueId) {
        List<ClueActivityRelation> carList = clueActivityRelationDao.getClueActivityRelationByClueID(clueId);

        List<Activity> activityList = activityDao.getActivityListByAid(carList);
        return activityList;
    }

    @Override
    public boolean deleteRelation(String clueId, String activityId) {
        boolean flag = true;
        int count = clueActivityRelationDao.deleteRelation(clueId,activityId);
        if (count == 0){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityNotRelationList(String clueId,String name) {
        List<Activity> activityList = null;
        List<ClueActivityRelation> carList = clueActivityRelationDao.getClueActivityRelationByClueID(clueId);
        activityList = activityDao.getActivityNotRelationList(carList,name);
        return activityList;
    }

    @Override
    public boolean bundActivity(String cid,String[] aids) {
        List<ClueActivityRelation> carList = new ArrayList<>();
        boolean flag = true;
        for (String aid:aids){
            ClueActivityRelation car = new ClueActivityRelation();
            String id = UUIDUtil.getUUID();
            car.setId(id);
            car.setClueId(cid);
            car.setActivityId(aid);
            carList.add(car);
        }
        int count = clueActivityRelationDao.bundActivity(carList);
        if (count == 0){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityList(String aname) {
        return activityDao.getActivityList(aname);
    }

    @Override
    public boolean convert(String clueId, String createBy, String createTime, Tran t) {
        boolean flag = true;
        //(1)获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);
        //(2)通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company =clue.getCompany();
        Customer cus = customerDao.getByName(company);
        if (cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(clue.getAddress());
            cus.setContactSummary(clue.getContactSummary());
            cus.setDescription(clue.getDescription());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setName(clue.getCompany());
            cus.setNextContactTime(clue.getNextContactTime());
            cus.setOwner(clue.getOwner());
            cus.setPhone(clue.getPhone());
            int count1 = customerDao.save(cus);
            if (count1 == 0){
                flag = false;
            }
        }
        //(3)通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setAddress(clue.getAddress());
        con.setContactSummary(clue.getContactSummary());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setDescription(clue.getDescription());
        con.setAppellation(clue.getAppellation());
        con.setCustomerId(cus.getId());
        con.setEmail(clue.getEmail());
        con.setFullname(clue.getFullname());
        con.setJob(clue.getJob());
        con.setMphone(clue.getMphone());
        con.setNextContactTime(clue.getNextContactTime());
        con.setOwner(clue.getOwner());
        con.setSource(clue.getSource());
        int count2 = contactsDao.save(con);
        if (count2 == 0){
            flag = false;
        }
        //(4)线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByCid(clueId);
        for(ClueRemark clueRemark:clueRemarkList){
            String noteContent = clueRemark.getNoteContent();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 == 0){
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setEditFlag("0");
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 == 0){
                flag = false;
            }
        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getClueActivityRelationByClueID(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 == 0){
                flag = false;
            }
        }
        //(6) 如果有创建交易需求，创建一条交易
        if (t != null){
            t.setOwner(clue.getOwner());
            t.setCustomerId(cus.getId());
            t.setContactsId(con.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setDescription(clue.getDescription());
            t.setNextContactTime(clue.getNextContactTime());
            t.setSource(clue.getSource());
            int count6 = tranDao.save(t);
            if (count6 == 0){
                flag = false;
            }else {
                //(7)如果创建了交易，则创建一条该交易下的交易历史
                TranHistory tranHistory = new TranHistory();
                tranHistory.setId(UUIDUtil.getUUID());
                tranHistory.setCreateBy(createBy);
                tranHistory.setCreateTime(createTime);
                tranHistory.setStage(t.getStage());
                tranHistory.setTranId(t.getId());
                tranHistory.setExpectedDate(t.getExpectedDate());
                tranHistory.setMoney(t.getMoney());
                int count7 = tranHistoryDao.save(tranHistory);
                if (count7 == 0){
                    flag =false;
                }
            }
        }

        //(8) 删除线索备注
        if (clueRemarkList.size()!=0){
            int count8 = clueRemarkDao.delete(clueId);
            if (count8 == 0){
                flag =false;
            }
        }

        if (clueActivityRelationList.size()!=0){
            //(9) 删除线索和市场活动的关系
            int count9 = clueActivityRelationDao.delete(clueId);
            if (count9 == 0){
                flag =false;
            }
        }

        //(10) 删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 == 0){
            flag =false;
        }

        return flag;
    }
}
