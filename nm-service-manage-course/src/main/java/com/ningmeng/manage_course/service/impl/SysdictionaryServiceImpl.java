package com.ningmeng.manage_course.service.impl;

import com.ningmeng.framework.domain.system.SysDictionary;
import com.ningmeng.manage_course.dao.SysDictionaryDao;
import com.ningmeng.manage_course.service.SysdictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2020/2/21.
 */
@Service
public class SysdictionaryServiceImpl implements SysdictionaryService {

    @Autowired
    private SysDictionaryDao sysDictionaryDao;
    @Override
    public SysDictionary findDictionaryByType(String type) {
        return sysDictionaryDao.findBydType(type);
    }
}
