package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2020/3/11.
 */
public interface NmuserDao extends JpaRepository<XcUser,String> {
    public  XcUser findByUsername(String name);
}
