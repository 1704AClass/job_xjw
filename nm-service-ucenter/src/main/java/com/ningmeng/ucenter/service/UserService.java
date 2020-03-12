package com.ningmeng.ucenter.service;

import com.ningmeng.framework.domain.ucenter.XcCompanyUser;
import com.ningmeng.framework.domain.ucenter.XcMenu;
import com.ningmeng.framework.domain.ucenter.XcUser;
import com.ningmeng.framework.domain.ucenter.ext.XcUserExt;
import com.ningmeng.ucenter.dao.NmCompanyUserDao;
import com.ningmeng.ucenter.dao.NmMenuMapper;
import com.ningmeng.ucenter.dao.NmuserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2020/3/11.
 */
@Service
public class UserService {

    @Autowired
    private NmuserDao nmuserDao;
    @Autowired
    private NmCompanyUserDao nmCompanyUserDao;
    @Autowired
    private NmMenuMapper nmMenuMapper;

    //根据用户账号查询信息
    private XcUser findByUsername(String username) {
        return nmuserDao.findByUsername( username );
    }

    //根据账号查询用户的信息,返回用户扩展信息
    public XcUserExt getUserExt(String username) {
        XcUser nmUser = this.findByUsername( username );
        if (nmUser == null) {
            return null;
        }
        String userId = nmUser.getId();
        List<XcMenu> nmMenus = nmMenuMapper.selectPermissionByUserId(userId);
        XcUserExt nmUserExt = new XcUserExt();
        BeanUtils.copyProperties(nmUser,nmUserExt);
        //用户的权限
        nmUserExt.setPermissions(nmMenus);
        return nmUserExt;
    }
}
