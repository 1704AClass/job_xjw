package com.ningmeng.auth.client;

import com.ningmeng.framework.client.NmServiceList;
import com.ningmeng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = " nm-service-ucenter")
public interface UserClient {
@GetMapping("/ucenter/getuserext")
public XcUserExt getUserext(@RequestParam("username") String username);
}