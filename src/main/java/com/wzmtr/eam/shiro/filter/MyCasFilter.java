package com.wzmtr.eam.shiro.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/4 9:57
 */
public class MyCasFilter extends CasFilter {

    private static final String TICKET_PARAMETER = "ticket";

    public MyCasFilter() {
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {

        WebUtils.getAndClearSavedRequest(request);
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());

        return false;
    }

/*    public AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        // 获取请求的ticket
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ticket = getRequestTicket(httpRequest);
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }
        return new CasToken(ticket);
    }*/

    /**
     * 拒绝除了option以外的所有请求
     **/

/*    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }*/


/*    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 获取ticket，如果不存在，直接返回false
        String ticket = getRequestTicket((HttpServletRequest) request);
        if (StringUtils.isEmpty(ticket)) {
            return false;
        }
        return this.executeLogin(request, response);
    }*/


    /*protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        //获取AuthenticationToken实体
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            //执行分子系统的Shrio认证与授权
            subject.login(token);
            SavedRequest shiroSavedRequest = (SavedRequest) SecurityUtils.getSubject().getSession(false).getAttribute("shiroSavedRequest");
            if (shiroSavedRequest != null) {
                //修改url地址为登录首页，否则会跳转到之前手输的地址容易404,由于没有set方法，所以使用反射
                Class<? extends Object> clazz  = shiroSavedRequest.getClass();
                Field requestURI = clazz.getDeclaredField("requestURI");
                requestURI.setAccessible(true);
                requestURI.set(shiroSavedRequest,this.getSuccessUrl());
            }
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }*/

    /**
     * 获取请求的ticket
     */
    private String getRequestTicket(HttpServletRequest httpRequest) {
        // 从参数中获取ticket
        String ticket = httpRequest.getParameter(TICKET_PARAMETER);
        if (StringUtils.isEmpty(ticket)) {
            // 如果为空的话，则从header中获取参数
            ticket = httpRequest.getHeader(TICKET_PARAMETER);
        }
        return ticket;
    }


}
