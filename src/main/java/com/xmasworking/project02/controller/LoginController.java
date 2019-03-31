package com.xmasworking.project02.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmasworking.project02.entity.Account;
import com.xmasworking.project02.entity.UserInfoEntity;
import com.xmasworking.project02.model.WeCharCode;
import com.xmasworking.project02.model.WeCharUserInfo;
import com.xmasworking.project02.repository.AccountRepository;
import com.xmasworking.project02.repository.UserInfoRepositiory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/17 - 下午3:27
 * Created by IntelliJ IDEA.
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserInfoRepositiory userInfoRepositiory;

    @RequestMapping
    public ModelAndView index(){
        return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc3597c3c72174c6d&redirect_uri=http%3a%2f%2fwww.medicalhelper.cn%2flogin%2fcode&response_type=code&scope=snsapi_userinfo&state=0#wechat_redirect"));
    }

    @RequestMapping("/code")
    public String getWecharCode(String code){
        String error = "";
        WeCharUserInfo weCharUserInfo = null;
        try {
            weCharUserInfo = sayHello(code);
            UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(weCharUserInfo.getNickname(),weCharUserInfo.getOpenid());
            Subject subject = SecurityUtils.getSubject();
            usernamePasswordToken.setRememberMe(true);
            //完成登录
            subject.login(usernamePasswordToken);
        }catch (Exception e){
            e.printStackTrace();
            error = e.getMessage();
        }
        return "main";
    }

    private WeCharUserInfo sayHello(String code) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid=wxc3597c3c72174c6d&secret=9720b9ba7c30d0db6ce9598fad7f0b72&code="+code+"&" +
                        "grant_type=authorization_code", String.class);

        String json = null;
        try {
            json = new String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        WeCharCode weCharCode=JSONObject.parseObject(json,WeCharCode.class);
        if(weCharCode.getErrcode() != null){
            throw new RuntimeException("get data error!!!");
        }

        ResponseEntity<String> responseEntity2 = restTemplate.getForEntity(
                "https://api.weixin.qq.com/sns/userinfo?" +
                        "access_token="+weCharCode.getAccess_token()+"&" +
                        "openid="+weCharCode.getOpenid()+"&lang=zh_CN", String.class);

        try {
            json = new String(responseEntity2.getBody().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        WeCharUserInfo weCharUserInfo=JSONObject.parseObject(json,WeCharUserInfo.class);

        this.saveAccountInfo(weCharCode, weCharUserInfo);
        System.out.println(weCharCode);
        System.out.println(weCharUserInfo);
        return weCharUserInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    void saveAccountInfo(WeCharCode weCharCode, WeCharUserInfo weCharUserInfo){

        Account accountEntity = new Account();
        accountEntity.setOpenid(String.valueOf(weCharCode.getOpenid()));

        Optional<Account> optional = accountRepository.findOne(Example.of(accountEntity));
        if(optional.isPresent()){
            accountEntity = optional.get();

        }
        accountEntity.setNickname(weCharUserInfo.getNickname());
        accountEntity.setExpires_in(weCharCode.getExpires_in());
        accountEntity.setAccess_token(weCharCode.getAccess_token());
        accountEntity.setRefresh_token(weCharCode.getRefresh_token());
        accountEntity.setSystem_time(System.currentTimeMillis());
        accountRepository.save(accountEntity);


        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setOpenid(weCharUserInfo.getOpenid());

        Optional<UserInfoEntity> optionalUserInfo = userInfoRepositiory.findOne(Example.of(userInfo));
        if(optionalUserInfo.isPresent()){
            userInfo = optionalUserInfo.get();
        }

        userInfo.setOpenid(weCharUserInfo.getOpenid());
        userInfo.setNickname(weCharUserInfo.getNickname());
        userInfo.setCity(weCharUserInfo.getCity());
        userInfo.setCountry(weCharUserInfo.getCountry());
        userInfo.setHeadimgurl(weCharUserInfo.getHeadimgurl());
        userInfo.setLanguage(weCharUserInfo.getLanguage());
        userInfo.setProvince(weCharUserInfo.getProvince());
        userInfo.setPrivilege(weCharUserInfo.getPrivilege());
        userInfo.setSex(weCharUserInfo.getSex());
        userInfo.setUnionid(weCharUserInfo.getUnionid());
        userInfoRepositiory.save(userInfo);

    }
}
