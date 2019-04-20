package com.xmasworking.project02.controller;

import com.xmasworking.project02.entity.Account;
import com.xmasworking.project02.entity.UseCode;
import com.xmasworking.project02.repository.CodeRepository;
import com.xmasworking.project02.repository.UseCodeRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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
    private final int LIMIT_SIZE = 80;

    @Autowired
    UseCodeRepository useCodeRepository;

    @Autowired
    CodeRepository codeRepository;

    @RequestMapping
    public String index(){
        if(isPass()){
            return "redirect:/showinfo";
        }
        return "main";
    }

    @RequestMapping("/checkcode")
    public ModelAndView checkcode(@RequestParam("invite_code") String inviteCode, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("main");
        httpSession.setAttribute("code",inviteCode);
        try {
            System.out.println(inviteCode);
            int isCode = codeRepository.countByCode(inviteCode.trim());

            //是否无效码
            if(isCode <=0){
                throw new Exception("投票码无效!!!");
            }

            //是否已投票
            if(isUseCode(inviteCode)){
                modelAndView = new ModelAndView("redirect:/showinfo");
            }

            int codeTimes = useCodeRepository.countByCodeAndStatus(inviteCode,1);
            if(codeTimes >= LIMIT_SIZE){
                throw new Exception("投票码已投满票数!!!");
            }

            modelAndView = new ModelAndView("redirect:/select");
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.addObject("msg", e.getMessage());

        }
        return modelAndView;
    }

    /**
     * 是否使用过code
     * @param inviteCode
     * @return
     */
    private boolean isUseCode(String inviteCode){
        Account account = (Account) SecurityUtils.getSubject().getPrincipal();
        UseCode useCode = useCodeRepository.findOneByOpenid(account.getOpenid());
        //已投过票
        if(useCode != null && useCode.getStatus() == 1){
            return Boolean.TRUE;
        }else if(useCode == null){
            useCode = new UseCode();
        }
        useCode.setCode(inviteCode);
        useCode.setOpenid(account.getOpenid());
        useCode.setStatus(0);
        useCodeRepository.save(useCode);
        return Boolean.FALSE;
    }

    /**
     * 是否使用过Code投票
     * @return
     */
    private boolean isPass(){
        Account account = (Account) SecurityUtils.getSubject().getPrincipal();
        int useCodeSize = useCodeRepository.countByOpenidAndStatus(account.getOpenid(),1);
        if(useCodeSize > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}


