package com.zh.server.service.impl;

import com.sun.tools.corba.se.idl.ExceptionEntry;
import com.zh.model.entity.ItemKillSuccess;
import com.zh.model.entity.KillSuccessUserInfo;
import com.zh.model.mapper.ItemKillSuccessMapper;
import com.zh.server.dto.MailDto;
import com.zh.server.service.MailService;
import com.zh.server.service.RabbitReceiverService;
import com.zh.server.service.RabbitSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Space_Pig
 * @date 2020/10/06 11:17
 */
@Service
public class RabbitReceiverServiceImpl implements RabbitReceiverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitSenderService.class);

    @Autowired
    MailServiceImpl mailService;
    @Autowired
    Environment env;
    @Resource
    ItemKillSuccessMapper itemKillSuccessMapper;
    /**
     * 秒杀消息通知-接受消息
     * @param info
     */
    @RabbitListener(queues = {"${mq.kill.item.success.email.queue}"},containerFactory = "singleListenerContainer")
    @Override
    public void consumerEmailMsg(KillSuccessUserInfo info) {
        LOGGER.info("秒杀消息异步通知-接受消息:{}",info);
        try {
            //TODO:正在发送邮件
            /*MailDto mailDto = new MailDto(env.getProperty("mail.kill.item.success.subject"),
                    env.getProperty("mail.kill.item.success.content"),new String[]{info.getEmail()});
            mailService.sendSimpleEmail(mailDto);*/
            final String context = String.format(env.getProperty("mail.kill.item.success.content"),
                    info.getItemName(),info.getCode());
            MailDto mailDto = new MailDto(env.getProperty("mail.kill.item.success.subject"),
                    context,new String[]{info.getEmail()});
            mailService.sendHtmlEmail(mailDto);
        }catch (Exception e){
            LOGGER.error("秒杀消息异步通知失败:{}",e.fillInStackTrace());
        }
    }


    /**
     * 用户秒杀成功后超时未支付-监听者
     * @param info
     */
    @RabbitListener(queues = {"${mq.kill.item.success.kill.dead.real.queue}"},containerFactory = "singleListenerContainer")
    @Override
    public void consumerExpireOrder(KillSuccessUserInfo info) {
        try {
            LOGGER.info("用户秒杀成功后超时未支付-监听者-接收消息:{}",info);
            if (info!=null){
                ItemKillSuccess entity=itemKillSuccessMapper.selectByPrimaryKey(info.getCode());
                if (entity!=null && entity.getStatus().intValue()==0){
                    itemKillSuccessMapper.expireOrder(info.getCode());
                }
            }
        }catch (Exception e){
            LOGGER.error("用户秒杀成功后超时未支付-监听者-发送异常:{}",e.fillInStackTrace());
        }
    }
}
