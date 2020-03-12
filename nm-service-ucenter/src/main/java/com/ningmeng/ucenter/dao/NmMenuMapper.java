package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2020/3/12.
 */
@Mapper
public interface NmMenuMapper {
    public List<XcMenu> selectPermissionByUserId(String userid);
}
