package com.xmasworking.project02.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmasworking.project02.model.WeCharCode;
import com.xmasworking.project02.model.WeCharUserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;

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

    @RequestMapping
    public ModelAndView index(){
        return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8047ed4e28fc1ae3&redirect_uri=http%3a%2f%2fwww.medicalhelper.cn%2flogin%2fcode&response_type=code&scope=snsapi_userinfo&state=0#wechat_redirect"));
    }

    @RequestMapping("/code")
    public ModelAndView getWecharCode(String code){
        ModelAndView modelAndView = new ModelAndView("index");
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
        modelAndView.addObject("Error",error);
        modelAndView.addObject("WeCharUserInfo",weCharUserInfo);
        return modelAndView;
    }

    @RequestMapping("/sayhello")
    @ResponseBody
    public WeCharUserInfo sayHello(String code) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid=wx8047ed4e28fc1ae3&secret=9720b9ba7c30d0db6ce9598fad7f0b72&code="+code+"&" +
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
            return new WeCharUserInfo();
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
        return weCharUserInfo;
    }
}
