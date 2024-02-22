package com.wzmtr.eam.shiro.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/4 9:57
 */
public class MyCasFilter extends CasFilter {

    private static final String TICKET_PARAMETER = "ticket";

    public MyCasFilter() {}

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.getAndClearSavedRequest(request);
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
        return false;
    }


}
