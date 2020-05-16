package com.ldq.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置类
 */

@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加Shiro内置过滤器，实现权限相关的拦截器
        /**
         *  参数
         *  anon：无需认证（登录）可以访问
         *  authc：必须认证才可以访问
         *  user：如果使用rememberMe的功能可以直接访问
         *  perms：该资源必须得到资源权限才可以访问
         *  role：该资源必须得到角色权限才可以访问
         */
        Map<String,String> filterMap = new LinkedHashMap<String,String>();

        // 无需认证可以访问
        filterMap.put("/login","anon");

        // 授权过滤器
        // 当前授权拦截后，shiro会自动跳转到未授权页面
        filterMap.put("/users/getAllUsers","perms[admin:visit]"); // 只有管理员可以访问
        filterMap.put("/api/document","perms[tea:visit]"); // 只有老师可以访问
        filterMap.put("/api/rank","perms[stu:visit]"); // 只有学生可以访问

        // 需要认证才能访问
//        filterMap.put("/users","authc");
//        filterMap.put("/roles","authc");
//        filterMap.put("/rights","authc");
        filterMap.put("/","user");

        // 修改跳转的登录页面
        shiroFilterFactoryBean.setLoginUrl("http://localhost:8080/#/login");
        // 设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("http://localhost:8080/#/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 关联realm
        securityManager.setRealm(userRealm);
        // 将cookie管理对象设置到SecurityManager
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean(name="userRealm")
    public UserRealm getRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
        UserRealm userRealm = new UserRealm();
        userRealm.setAuthorizationCachingEnabled(false);
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    /**
     * shiro在进行密码验证的时候，将会在此进行匹配
     */
    @Bean(name="hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列一次，相当于md5();
        //hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64；
        return hashedCredentialsMatcher;
    }

    /**
     * cookie对象
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        System.out.println("ShiroConfiguration.rememberMeCookie()");
        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    /**
     * cookie管理对象
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }
}
