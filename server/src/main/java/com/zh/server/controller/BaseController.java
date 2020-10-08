package com.zh.server.controller;

import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author Space_Pig
 * @date 2020/09/30 10:51
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping("/hello")
    public String hello(String msg, ModelMap map){
        if (StringUtil.isBlank(msg))
            msg = "测试MVC";
        map.put("msg",msg);
        return "hello";
    }

    @RequestMapping(value = "/error",method = RequestMethod.GET)
    public String error(){
        return "/error";
    }


}
