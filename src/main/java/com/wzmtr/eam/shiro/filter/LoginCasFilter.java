package com.wzmtr.eam.shiro.filter;

import org.apache.shiro.cas.CasFilter;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/3 19:27
 */
@SuppressWarnings("deprecation")
public class LoginCasFilter extends CasFilter {
/*    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {

        WebUtils.getAndClearSavedRequest(request);
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());

        return false;
    }*/

}
