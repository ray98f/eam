package com.wzmtr.eam.utils;

import com.wzmtr.eam.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Cookie 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/24
 */
@Slf4j
public final class CookieUtils {

    /**
     * 得到Cookie的值, 不编码
     * @param request    request
     * @param cookieName cookie 名称
     * @return Cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到Cookie的值
     * @param request    request
     * @param cookieName cookie 名称
     * @param isDecoder  是否编码
     * @return Cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } else {
                        retValue = cookie.getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("exception message", e);
        }
        return retValue;
    }

    /**
     * 得到Cookie的值
     * @param request      request
     * @param cookieName   cookie 名称
     * @param encodeString 编码类型
     * @return Cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookie.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("exception message", e);
        }
        return retValue;
    }

    /**
     * 设置Cookie的值 不设置生效时间默认浏览器关闭即失效,也不编码
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 设置Cookie的值 在指定时间内生效,但不编码
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie生效的最大秒数
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }

    /**
     * 设置Cookie的值 不设置生效时间,但编码
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param isEncode 是否编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie生效的最大秒数
     * @param isEncode 是否编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }

    /**
     * 设置Cookie的值 在指定时间内生效, 编码参数(指定编码)
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie生效的最大秒数
     * @param encodeString 编码类型
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }

    /**
     * 删除Cookie带cookie域名
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie生效的最大秒数
     * @param isEncode 是否编码
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            }
            if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            // 设置域名的cookie
            if (null != request) {
                String domainName = getDomainName(request);
                if (!CommonConstants.LOCALHOST.equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error("exception message", e);
        }
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效
     * @param request request
     * @param response response
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie生效的最大秒数
     * @param encodeString 编码类型
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String cookieValue, int cookieMaxAge, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge > CommonConstants.ZERO) {
                cookie.setMaxAge(cookieMaxAge);
            }
            // 设置域名的cookie
            if (null != request) {
                String domainName = getDomainName(request);
                if (!CommonConstants.LOCALHOST.equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            log.error("exception message", e);
        }
    }

    /**
     * 得到cookie的域名
     * @param request request
     * @return 域名
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName;

        String serverName = request.getRequestURL().toString();
        if (StringUtils.isEmpty(serverName)) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            if (serverName.startsWith(CommonConstants.HTTP)) {
                serverName = serverName.substring(7);
            } else if (serverName.startsWith(CommonConstants.HTTPS)) {
                serverName = serverName.substring(8);
            }
            final int end = serverName.indexOf(":");
            System.out.println(end + " end......");
            serverName = serverName.substring(0, end);
            System.out.println(serverName + " Server........");
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > CommonConstants.THREE) {
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > CommonConstants.ONE) {
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (StringUtils.isNotEmpty(domainName) && domainName.indexOf(CommonConstants.COLON) > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;

    }

}