package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.system.SysDictionary;

/**
 * Created by Administrator on 2020/2/21.
 */
public interface SysdictionaryService {

    SysDictionary findDictionaryByType(String type);
}
