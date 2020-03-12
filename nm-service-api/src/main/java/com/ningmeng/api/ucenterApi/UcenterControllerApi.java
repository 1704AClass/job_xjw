package com.ningmeng.api.ucenterApi;

import com.ningmeng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Administrator on 2020/3/11.
 */
@Api(value = "用户中心",description = "用户中心管理")
public interface UcenterControllerApi {

    @ApiOperation( "根据账号查询用户信息" )
    public XcUserExt getUserext(String username);
}
