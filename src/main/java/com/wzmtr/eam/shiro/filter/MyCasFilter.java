package com.wzmtr.eam.shiro.filter;

import com.wzmtr.eam.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

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

    public MyCasFilter() {}

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.getAndClearSavedRequest(request);
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
        return false;
    }

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
