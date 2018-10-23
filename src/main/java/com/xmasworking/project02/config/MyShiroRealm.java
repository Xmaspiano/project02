package com.xmasworking.project02.config;

import com.xmasworking.project02.entity.Account;
import com.xmasworking.project02.repository.AccountRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *
 *
 * @author XmasPiano
 * @date 2018/3/1 上午10:16
 * @param
 * @return
 */
public class MyShiroRealm extends AuthorizingRealm implements CacheManagerAware {
    @Value("${admin.loginid}")
    private String adminName;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.lastname}")
    private String lastname;

    @Autowired
    AccountRepository accountRepository;

    /**
     * 认证.登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的token
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        String username = utoken.getUsername();

        Account accountEntity = new Account();

        //如果用户名是管理员
        if(this.adminName.equals(username)) {
            accountEntity.setOpenid(password);
            accountEntity.setNickname(adminName);
            //验证管理员
            return new SimpleAuthenticationInfo(accountEntity, this.password, getName());
        }

        //验证用户
        try {
            accountEntity.setOpenid(String.valueOf(utoken.getPassword()));

            Optional<Account> optional = accountRepository.findOne(Example.of(accountEntity));
            if(optional.isPresent()){
                accountEntity = optional.get();
            }else {
                accountEntity.setNickname(username);
                accountEntity = accountRepository.save(accountEntity);
            }
        }catch (NoSuchElementException nee){
            nee.printStackTrace();
            throw new UnknownAccountException(nee.getMessage());
        }

        return new SimpleAuthenticationInfo(accountEntity, accountEntity.getOpenid(), this.getClass().getName());


    }

    /**
     * 授权
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");
        Account accountEntity = (Account) SecurityUtils.getSubject().getPrincipal();
        if(this.adminName.equals(accountEntity.getNickname())) {
            authorizationInfo.addRole("manager");
        }
        return authorizationInfo;
    }

    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

}