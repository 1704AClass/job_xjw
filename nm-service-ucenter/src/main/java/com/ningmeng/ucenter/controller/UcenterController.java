package com.ningmeng.ucenter.controller;

import com.ningmeng.api.ucenterApi.UcenterControllerApi;
import com.ningmeng.framework.domain.ucenter.ext.XcUserExt;
import com.ningmeng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2020/3/11.
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {
    @Autowired
    UserService userService;
    @Override
    @GetMapping("/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username) {
        XcUserExt nmUser = userService.getUserExt(username);
        return nmUser;
    }
}
