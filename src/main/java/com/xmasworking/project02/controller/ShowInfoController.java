package com.xmasworking.project02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/30 - 上午10:23
 * Created by IntelliJ IDEA.
 */
@Controller
public class ShowInfoController {

    @RequestMapping("/showinfo")
    public String index(){
        return "showinfo";
    }

    @RequestMapping("/tpsm")
    public String tpsm(){
        return "tpsm";
    }

    @RequestMapping("/ysx")
    public String ysx(){
        return "ysx";
    }

    @RequestMapping("/end")
    public String end(){
        return "end";
    }

}
