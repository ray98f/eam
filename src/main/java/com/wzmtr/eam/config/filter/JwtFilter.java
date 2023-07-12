package com.wzmtr.eam.config.filter;

import com.wzmtr.eam.config.RequestHeaderContext;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.TokenStatus;
import com.wzmtr.eam.utils.TokenUtil;
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

    @Value("${excluded.pages}")
    private String[] pages;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (Arrays.asList(pages).contains(uri)) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String token = httpRequest.getHeader("Authorization");
            if (token == null || StringUtils.isBlank(token)) {
                request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_EMPTY));
                request.getRequestDispatcher("/error/exthrow").forward(request, response);
                return;
            }
            TokenStatus tokenStatus = TokenUtil.verifySimpleToken(token);
            switch (Objects.requireNonNull(tokenStatus)) {
                //有效
                case VALID:
                    CurrentLoginUser simpleTokenInfo = TokenUtil.getSimpleTokenInfo(token);
                    if (simpleTokenInfo == null) {
                        request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_CHECK_FAIL));
                        request.getRequestDispatcher("/error/exthrow").forward(request, response);
                        break;
                    }
                    new RequestHeaderContext.RequestHeaderContextBuild().user(simpleTokenInfo).build();
                    httpRequest.setAttribute("tokenInfo", simpleTokenInfo);
                    chain.doFilter(httpRequest, httpResponse);
                    break;
                //过期
                case EXPIRED:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_IS_OVERDUE));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
                    break;
                //无效
                default:
                    request.setAttribute("filter.error", new CommonException(ErrorCode.AUTHORIZATION_INVALID));
                    request.getRequestDispatcher("/error/exthrow").forward(request, response);
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
