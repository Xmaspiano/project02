package com.xmasworking.project02.controller;

import com.xmasworking.project02.repository.ShowUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * @author XmasPiano
 * @date 2018/8/30 - 上午10:23
 * Created by IntelliJ IDEA.
 */
@Controller
@RequestMapping("/select")
public class SelectController {

    @Autowired
    ShowUserInfoRepository showUserInfoRepository;

    @RequestMapping
    public ModelAndView index() {

        //如果投过票则显示已投
        if(Boolean.FALSE){
            ModelAndView modelAndView = new ModelAndView("redirect:/success");
            return modelAndView;
        }

        //如果没投过则进入投票页面
        ModelAndView modelAndView = new ModelAndView("select");
        modelAndView.addObject("stringList", showUserInfoRepository.findAll());
        return modelAndView;
    }

    @RequestMapping("commit")
    public String commit(@RequestParam(value = "ids[]") Long[] ids){
        //记录投票信息
        //投票成功
        return "redirect:/success";
    }

}
