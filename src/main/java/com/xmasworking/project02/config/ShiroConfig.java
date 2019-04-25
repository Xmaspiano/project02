package com.xmasworking.project02.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/31 - 下午5:07
 * Created by IntelliJ IDEA.
 */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/end");
        bean.setSuccessUrl("/main");
//        bean.setUnauthorizedUrl("/system/index");

        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        bean.setFilters(filtersMap);

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<String, String>();
        //拦截器.

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout**","logout");

        filterChainDefinitionMap.put("/end/**","anon");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/src/**", "anon");

        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }

    @Bean
    public EnterpriseCacheSessionDAO enterpriseCacheSessionDAO(){
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        return enterpriseCacheSessionDAO;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(enterpriseCacheSessionDAO());
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    //配置自定义的密码比较器
    @Bean
    @Primary
    public CredentialsMatcher credentialsMatcher() {
        return new MyCredentialsMatcher();
    }
}