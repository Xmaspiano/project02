package com.xmasworking.project02.controller;

import com.xmasworking.project02.entity.Account;
import com.xmasworking.project02.entity.Commit;
import com.xmasworking.project02.entity.UseCode;
import com.xmasworking.project02.repository.CommitRepository;
import com.xmasworking.project02.repository.ShowUserInfoRepository;
import com.xmasworking.project02.repository.UseCodeRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    CommitRepository commitRepository;

    @Autowired
    UseCodeRepository useCodeRepository;

    @RequestMapping
    public ModelAndView index() {
        Account account = (Account)SecurityUtils.getSubject().getPrincipal();

        UseCode useCode = useCodeRepository.findOneByOpenid(account.getOpenid());
        if(useCode == null || "".equals(useCode.getCode())){
            ModelAndView modelAndView = new ModelAndView("redirect:/main");
            return modelAndView;
        }

        //如果投过票则显示已投

        int length = commitRepository.countByOpenid(account.getOpenid());
        if(length >= 10){
            ModelAndView modelAndView = new ModelAndView("redirect:/showinfo");
            return modelAndView;
        }

        //如果没投过则进入投票页面
        ModelAndView modelAndView = new ModelAndView("select");
        modelAndView.addObject("stringList", showUserInfoRepository.findAll());

        return modelAndView;
    }

    @RequestMapping("commit")
    @ResponseBody
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Map commit(@RequestParam(value = "ids[]") Long[] ids,HttpSession httpSession){
        String code = (String) httpSession.getAttribute("code");

        Map map = new HashMap(16);
        if(ids.length != 10){
            map.put("status", false);
            map.put("error", "票数异常，请投10票!!!");
            return map;
        }

        Account account = (Account)SecurityUtils.getSubject().getPrincipal();
        UseCode useCode = useCodeRepository.findOneByOpenid(account.getOpenid());
        if(useCode != null && useCode.getCode().equals(code)){
            int codeTimes = useCodeRepository.countByCodeAndStatus(code,1);
            if(codeTimes >= 80){
                map.put("forword", "/ysx");
                map.put("status", true);
                map.put("msg", "投票码已投满票数!!!");
                return map;
            }

            int length = commitRepository.countByOpenid(account.getOpenid());
            if(length >= 10){
                map.put("forword", "/showinfo");
                map.put("status", true);
                map.put("msg", "已投票!!!");
                return map;
            }
        }else {
            System.out.println("投票码异常!!!"+useCode);
        }
        useCode.setStatus(1);
        useCodeRepository.save(useCode);


        int length = commitRepository.countByOpenid(account.getOpenid());
        if(length < 10){
        //记录投票信息
            List<Commit> commits = new ArrayList<>();
            Commit commit;
            for(Long showid:ids){
                commit = new Commit();
                commit.setOpenid(account.getOpenid());
                commit.setShowid(showid);
                commits.add(commit);
            }
            commitRepository.saveAll(commits);
        }
        //投票成功
        map.put("forword", "/rank");
        map.put("status", true);
        map.put("msg", "登录成功");
        return map;
    }

}
