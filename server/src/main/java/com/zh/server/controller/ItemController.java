package com.zh.server.controller;


import com.zh.model.entity.ItemKill;
import com.zh.server.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @author Space_Pig
 * @date 2020/10/01 8:30
 */
@Controller
public class ItemController {
    //获取类的错误信息，以日志形式输出
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    private static final String prefix = "item";

    @Autowired
    private ItemService itemService;


    /**
     * 查询所有的秒杀商品
     * @param modelMap
     * @return
     */

    @RequestMapping(value = {"/","index",prefix+"/list",prefix+"/index.html"},method = RequestMethod.GET)
    public String list(ModelMap modelMap){
        try {
            List<ItemKill> itemKill = itemService.getItemKill();
            LOGGER.info("查询到的秒杀商品信息{}",itemKill);
            modelMap.put("list",itemKill);
        }catch (Exception e){
            LOGGER.error("获取商品信息异常！！"+e.fillInStackTrace());
            return "redirect:/error";
        }
        return "list";
    }

    /**
     * 查询秒杀商品的详情
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = prefix+"/info/{id}",method = RequestMethod.GET)
    public String info(@PathVariable Integer id, ModelMap modelMap){
        if (id==null || id < 0){
            return "redirect:/error";
        }
        try {
            ItemKill itemKillInfo = itemService.getItemKillInfo(id);
            LOGGER.info("查询到的秒杀商品的详细信息{}",itemKillInfo);
            modelMap.put("detail",itemKillInfo);
        }catch (Exception e){
            LOGGER.error("查询到的秒杀商品的详细信息！！" + e.fillInStackTrace());
            return "redirect:/error";
        }
        return "info";
    }



}
