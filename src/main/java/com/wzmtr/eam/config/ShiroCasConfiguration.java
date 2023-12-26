package com.wzmtr.eam.config;

import com.wzmtr.eam.shiro.realm.ShiroCasRealm;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/4 9:58
 */
@Configuration
public class ShiroCasConfiguration {

    /**
     * 的server地址
     */
    @Value("${cas.serverUrlPrefix}")
    public String casServerUrlPrefix;

    /**
     * 对外提供的服务地址
     */
    @Value("${cas.serviceUrlPrefix}")
    public String casServiceUrlPrefix;

    /**
     * casFilter cas 拦截的地址
     */
    @Value("${cas.casFilterUrlPattern}")
    public String casFilterUrlPattern;

    /**
     * 登录成功的地址
     */
    @Value("${cas.successUrlPattern}")
    public String loginSuccessUrl;

    /**
     * 登录的地址
     */
    @Value("${cas.loginUrlPattern}")
    public String loginUrlPattern;

    /**
     * 退出的地址
     */
    @Value("${cas.logoutUrlPattern}")
    public String logoutUrlPattern;

    public static final String UNAUTHORIZED_URL = "/error/exthrow";

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(shiroCasRealm());
        defaultWebSecurityManager.setSubjectFactory(new CasSubjectFactory());
        return defaultWebSecurityManager;
    }

    @Bean
    public ShiroCasRealm shiroCasRealm() {
        ShiroCasRealm shiroCasRealm = new ShiroCasRealm();
        shiroCasRealm.setCasServerUrlPrefix(casServerUrlPrefix);
        shiroCasRealm.setCasService(casServiceUrlPrefix + casFilterUrlPattern);
        return shiroCasRealm;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean  bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        bean.setEnabled(true);
        return bean;
    }

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setName("registrationBean");
        registrationBean.setFilter(new SingleSignOutFilter());
        //拦截所有的请求
        registrationBean.addUrlPatterns("/*");
        registrationBean.setEnabled(true);
        //设置优先级
        registrationBean.setOrder(10);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new DelegatingFilterProxy("shiroFilter"));
        bean.addInitParameter("targetFilterLifecycle", "true");
        bean.setEnabled(true);
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor attributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 使用工厂模式，创建并初始化ShiroFilter
     * @param securityManager
     * @param casFilter
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl(String.join("", casServerUrlPrefix, loginUrlPattern,
                "?service=",casServiceUrlPrefix,casFilterUrlPattern));
        //factoryBean.setSuccessUrl(loginSuccessUrl);
        factoryBean.setUnauthorizedUrl(String.join("", casServerUrlPrefix, loginUrlPattern,
                "?service=",casServiceUrlPrefix,casFilterUrlPattern));
        Map<String, Filter> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("casFilter", casFilter);

        factoryBean.setFilters(linkedHashMap);
        loadShiroFilterChain(factoryBean);
        return factoryBean;
    }

    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        casFilter.setFailureUrl(String.join("", casServerUrlPrefix, loginUrlPattern,
                "?service=",casServiceUrlPrefix,casFilterUrlPattern));
        casFilter.setSuccessUrl(casServiceUrlPrefix + loginSuccessUrl);
        return casFilter;

    }

    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");
        filterChainDefinitionMap.put("/rights/index", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

}
