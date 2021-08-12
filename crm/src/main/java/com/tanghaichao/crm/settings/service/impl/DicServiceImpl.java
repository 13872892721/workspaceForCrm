package com.tanghaichao.crm.settings.service.impl;

import com.tanghaichao.crm.settings.dao.DicTypeDao;
import com.tanghaichao.crm.settings.dao.DicValueDao;
import com.tanghaichao.crm.settings.domain.DicType;
import com.tanghaichao.crm.settings.domain.DicValue;
import com.tanghaichao.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao;
    private DicValueDao dicValueDao;

    public void setDicTypeDao(DicTypeDao dicTypeDao) {
        this.dicTypeDao = dicTypeDao;
    }

    public void setDicValueDao(DicValueDao dicValueDao) {
        this.dicValueDao = dicValueDao;
    }

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();
        List<DicType> dtList = dicTypeDao.getType();

        for (DicType dt:dtList){
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getValue(code);
            map.put(code,dvList);
        }
        return map;
    }
}
