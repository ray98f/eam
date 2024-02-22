package com.wzmtr.eam.config.filter;

import com.wzmtr.eam.config.RequestHeaderContext;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.TokenStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class JwtFilter implements Filter {

    private static final String JSESSIONID_FIX = ";jsessionid";
    private static final String FILTER_ERROR= "filter.error";
    private static final String ERROR_EXTHROW = "error/exthrow";

    @Value("${excluded.swagger-pages}")
    private String[] swaggerPages;

    @Value("${excluded.pages}")
    private String[] pages;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(uri.contains(JSESSIONID_FIX)){
            String newUri = uri.substring(0, uri.indexOf(";"));
            String jsessionid = uri.replace(newUri+";jsessionid=","");
            httpRequest.getSession();
            Cookie cookie = new Cookie("JSESSIONID", jsessionid);
            cookie.setHttpOnly(true);
            httpResponse.addCookie(cookie);
            httpResponse.sendRedirect(newUri);
            return;
        }
        if (Arrays.asList(pages).contains(uri) || Arrays.asList(swaggerPages).contains(uri)
                || uri.contains("mdmSync") || uri.contains("swagger")) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || org.apache.commons.lang3.StringUtils.isBlank(token)) {
                request.setAttribute(FILTER_ERROR, new CommonException(ErrorCode.AUTHORIZATION_EMPTY));
                request.getRequestDispatcher(ERROR_EXTHROW).forward(request, response);
                return;
            }
            TokenStatus tokenStatus = TokenUtils.verifySimpleToken(token);
            switch (Objects.requireNonNull(tokenStatus)) {
                //有效
                case VALID:
                    CurrentLoginUser simpleTokenInfo = TokenUtils.getSimpleTokenInfo(token);
                    if (simpleTokenInfo == null) {
                        request.setAttribute(FILTER_ERROR, new CommonException(ErrorCode.AUTHORIZATION_CHECK_FAIL));
                        request.getRequestDispatcher(ERROR_EXTHROW).forward(request, response);
                        break;
                    }
                    new RequestHeaderContext.RequestHeaderContextBuild().user(simpleTokenInfo).build();
                    httpRequest.setAttribute("tokenInfo", simpleTokenInfo);
                    chain.doFilter(httpRequest, httpResponse);
                    break;
                //过期
                case EXPIRED:
                    request.setAttribute(FILTER_ERROR, new CommonException(ErrorCode.AUTHORIZATION_IS_OVERDUE));
                    request.getRequestDispatcher(ERROR_EXTHROW).forward(request, response);
                    break;
                //无效
                default:
                    request.setAttribute(FILTER_ERROR, new CommonException(ErrorCode.AUTHORIZATION_INVALID));
                    request.getRequestDispatcher(ERROR_EXTHROW).forward(request, response);
                    break;
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("jwtFilter init ...");
    }

    @Override
    public void destroy() {
        log.info("jwtFilter destroy ...");
    }
}
