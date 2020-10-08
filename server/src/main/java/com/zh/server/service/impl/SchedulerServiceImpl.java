package com.zh.server.service.impl;

import com.zh.model.entity.ItemKillSuccess;
import com.zh.model.mapper.ItemKillSuccessMapper;
import com.zh.server.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Space_Pig
 * @date 2020/10/07 17:11
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Resource
    private ItemKillSuccessMapper itemKillSuccessMapper;
    @Resource
    private Environment env;

    /**
     * 为了避免rabbitmq的宕机，订单的丢失
     * 定时器定时处理秒杀成功后的超时订单
     *
     * 0 0/30 * * * ?
     */
    @Scheduled(cron = "0/10 * * * * ? ")
    @Override
    public void schedulerExpireOrders() {
        try {
            //获取批量秒杀成功但为处理的订单
            List<ItemKillSuccess> list = itemKillSuccessMapper.selectExpireOrders();
            //java8的遍历并判断
            if (list != null && !list.isEmpty()){
                list.stream().forEach(i ->{
                    if (i!=null && i.getDiffTime() > env.getProperty("scheduler.expire.orders.time",Integer.class));
                    itemKillSuccessMapper.expireOrder(i.getCode());
                });
            }
        }catch (Exception e){
            LOGGER.error("定时任务判断订单是否超时并失效-失败：{}",e.fillInStackTrace());
        }
    }
}
