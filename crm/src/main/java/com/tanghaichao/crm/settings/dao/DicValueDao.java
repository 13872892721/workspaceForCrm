package com.tanghaichao.crm.settings.dao;

import com.tanghaichao.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValue(String code);
}
