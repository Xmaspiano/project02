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
@RequestMapping("/showinfo")
public class ShowInfoController {

    @RequestMapping
    public String index(){
        return "showinfo";
    }
}
