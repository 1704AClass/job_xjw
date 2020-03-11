package com.ningmeng.auth.controller;

import com.ningmeng.api.authApi.AuthControllerApi;
import com.ningmeng.auth.service.AuthService;
import com.ningmeng.framework.domain.ucenter.ext.AuthToken;
import com.ningmeng.framework.domain.ucenter.request.LoginRequest;
import com.ningmeng.framework.domain.ucenter.response.AuthCode;
import com.ningmeng.framework.domain.ucenter.response.LoginResult;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2020/3/10.
 */
@RestController
public class AuthController implements AuthControllerApi {
    @Value( "${auth.clientId}" )
    private String clientId;
    @Value( "${auth.clientSecret}" )
    private String clienSecret;
    @Value( "${auth.cookieDomain}" )
    private String cookieDomain;
    @Value( "${auth.cookieMaxAge}" )
    private int cookieMaxAge;
    @Value( "${auth.tokenValiditySeconds" )
    private int tokenValiditySeconds;
    @Autowired
    private AuthService authService;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        //校验账号是否输入
            if(loginRequest == null ||
                    StringUtils.isEmpty(loginRequest.getUsername())){
                ExceptionCast.cast( AuthCode.AUTH_USERNAME_NONE);
            }
            //校验密码是否输入
            if(StringUtils.isEmpty(loginRequest.getPassword())){
                ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
            }
            AuthToken authToken = authService.login(loginRequest.getUsername(),
                    loginRequest.getPassword(), clientId, clienSecret);
        //将令牌写入cookie
        // 访问token
        String access_token = authToken.getAccess_token();
        //将访问令牌存储到cookie
        this.saveCookie(access_token);
        return new LoginResult( CommonCode.SUCCESS,access_token);
    }
    //将令牌保存到cookie
    private void saveCookie(String access_token)
    {
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        //添加cookie 认证令牌，最后一个参数设置为false，表示允许浏览器获取
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", access_token, cookieMaxAge, false);
    }


    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        return null;
    }
}
