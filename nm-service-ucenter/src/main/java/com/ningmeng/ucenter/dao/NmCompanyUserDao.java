package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2020/3/11.
 */
public interface NmCompanyUserDao  extends JpaRepository<XcCompanyUser,String>{
    public  XcCompanyUser findByUserId(String userId);
}
