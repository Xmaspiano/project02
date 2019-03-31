package com.xmasworking.project02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/30 - 上午10:23
 * Created by IntelliJ IDEA.
 */
@Controller
@RequestMapping("/main")
public class MainController {

    @RequestMapping
    public String index(){
        return "main";
    }

    @RequestMapping("/checkcode")
    public ModelAndView checkcode(@RequestParam("invite_code") String inviteCode, HttpSession httpSession){
        if(isRealCode(inviteCode)){
            ModelAndView modelAndView = new ModelAndView("redirect:/select");
            return modelAndView;
        }else{
            ModelAndView modelAndView = new ModelAndView("main");
            modelAndView.addObject("msg", "投票码已投满票数！！！");
            return modelAndView;
        }
    }

    /**
     * 拦截逻辑
     * @param submit_code
     * @return
     */
    private boolean isRealCode(String submit_code){
        //投票次数超过500则不能投票
        return Boolean.TRUE;
    }

}
