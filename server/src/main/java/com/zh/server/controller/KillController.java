package com.zh.server.controller;

import com.zh.api.enums.StatusCode;
import com.zh.api.response.BaseResponse;
import com.zh.model.entity.KillSuccessUserInfo;
import com.zh.model.mapper.ItemKillSuccessMapper;
import com.zh.server.dto.KillDto;
import com.zh.server.service.KillService;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author Space_Pig
 * @date 2020/10/05 16:16
 */
@Controller
public class KillController {
    public static final Logger LOGGER = LoggerFactory.getLogger(KillController.class);
    public static final String prefix = "kill";

    @Autowired
    private KillService killService;

    @Resource
    private ItemKillSuccessMapper itemKillSuccessMapper;

    /**
     * 判断商品是否秒杀成功
     * @param killDto
     * @param result
     * @param session
     * @return
     */
    @RequestMapping(value = prefix+"/execute",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BaseResponse execute(@RequestBody @Validated KillDto killDto, BindingResult result, HttpSession session){
        if (result.hasErrors() || killDto.getKillId()<0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        Object uid = session.getAttribute("uid");
        if (uid == null){
            return new BaseResponse(StatusCode.UserNotLogin);
        }
        //获取用户的id
        Integer userId = (Integer)uid;
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            Boolean res = killService.killItem(killDto.getKillId(), userId);
            if (!res)
                baseResponse = new BaseResponse(StatusCode.Fail.getCode(),"哈哈~商品已抢购完毕或者不在抢购时间段哦!");
        }catch (Exception e){
            baseResponse = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return baseResponse;
    }

    /**
     * 判断商品是否秒杀成功----->进行压力测试
     * @param killDto
     * @param result
     * @param session
     * @return
     */
    @RequestMapping(value = prefix+"/execute/lock",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BaseResponse executeLock(@RequestBody @Validated KillDto killDto, BindingResult result, HttpSession session){
        if (result.hasErrors() || killDto.getKillId()<0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            //不加分布式锁，出现超卖问题
            /*Boolean res = killService.killItemV2(killDto.getKillId(), killDto.getUserId());
            if (!res)
                baseResponse = new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁！！哈哈~商品已抢购完毕或者不在抢购时间段哦!");*/
            //加Redis分布式锁
            /*Boolean res = killService.killItemV3(killDto.getKillId(), killDto.getUserId());
            if (!res)
                baseResponse = new BaseResponse(StatusCode.Fail.getCode(),
                        "加Redis分布式锁！！哈哈~商品已抢购完毕或者不在抢购时间段哦!");*/
            //加redisson的分布式锁
            Boolean res = killService.killItemV3(killDto.getKillId(), killDto.getUserId());
            if (!res)
                return new BaseResponse(StatusCode.Fail.getCode(),
                        "不加分布式锁！！哈哈~商品已抢购完毕或者不在抢购时间段哦!");
        }catch (Exception e){
            baseResponse = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return baseResponse;
    }

    /**
     * 商品秒杀成功
     * @return
     */
    @RequestMapping(value = prefix+"/execute/success",method = RequestMethod.GET)
    public String success(){
        return "executeSuccess";
    }

    /**
     * 商品秒杀失败
     * @return
     */
    @RequestMapping(value = prefix+"/execute/fail",method = RequestMethod.GET)
    public String fail(){
        return "executeFail";
    }
    //    //http://localhost:8092/Seckill/kill/record/detail/511271935808909312

    @RequestMapping(value = prefix+"/record/detail/{orderNum}",method = RequestMethod.GET)
    public String killRecord(@PathVariable String orderNum, ModelMap modelMap){
        if (StringUtil.isBlank(orderNum)){
            return "error";
        }
        KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderNum);
        modelMap.put("info",info);
        return "killRecord";
    }
}
