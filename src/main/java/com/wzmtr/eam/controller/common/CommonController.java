package com.wzmtr.eam.controller.common;

import com.wzmtr.eam.entity.response.DataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@Slf4j
@RestController
@Api(tags = "公共分类")
@Validated
public class CommonController {

    /**
     * cas 的server地址
     */
    @Value("${cas.serverUrlPrefix}")
    public String casServerUrlPrefix;

    /**
     * 对外提供的服务地址
     */
    @Value("${cas.serviceUrlPrefix}")
    public String casServiceUrlPrefix;

    @Value("${cas.casFilterUrlPattern}")
    public String casFilterUrlPattern;
    @Value("${cas.loginUrlPattern}")
    public String loginUrlPattern;

    /**
     * 退出的地址
     */
    @Value("${cas.logoutUrlPattern}")
    public String logoutUrlPattern;

    @Value("${cas.serviceFront}")
    private String homeUrl;

    @Value("${sso.home}")
    private String home;

    @ApiOperation(value = "登出")
    @GetMapping(value = "/logout")
    public DataResponse<String> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return DataResponse.of(String.join("", casServerUrlPrefix, logoutUrlPattern,
                "?service=", home));
    }

    @ApiOperation(value = "登录")
    @GetMapping(value = "/login")
    public DataResponse<String> login() {
        return DataResponse.of(String.join("", casServerUrlPrefix, loginUrlPattern,
                "?service=", casServiceUrlPrefix, casFilterUrlPattern));
    }

    @GetMapping(value = "/")
    public void reload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.join("", casServerUrlPrefix, loginUrlPattern,
                "?service=", casServiceUrlPrefix, casFilterUrlPattern));
    }

    @GetMapping(value = "/index")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(homeUrl + "?token=" + request.getSession().getAttribute("jwtToken"));
    }

    @ApiOperation(value = "eip首页")
    @GetMapping(value = "/home")
    public DataResponse<String> home() {
        return DataResponse.of(home);
    }

    @RequestMapping("/error/exthrow")
    public void rethrow(HttpServletRequest request) throws Throwable {
        throw (Throwable) request.getAttribute("filter.error");
    }
}
