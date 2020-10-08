package com.zh.server.service.impl;

import com.zh.model.entity.KillSuccessUserInfo;
import com.zh.model.mapper.ItemKillSuccessMapper;
import com.zh.server.service.RabbitSenderService;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Space_Pig
 * @date 2020/10/04 23:10
 */
@Service
public class RabbitSenderServiceImpl implements RabbitSenderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitSenderServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;
    /**
     * 秒杀成功异步发送邮件通知
     * @param orderNo
     */
    @Override
    public void sendKillSuccessEmailMsg(String orderNo) {

        LOGGER.info("秒杀成功异步发送邮件通知{}",orderNo);
        try{
            if (StringUtil.isNotBlank(orderNo)){
                KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderNo);
                if (info != null){
                    //TODO:RabbitMQ发送消息
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));
                    /*Message msg = MessageBuilder.withBody(orderNo.getBytes("UTF-8")).build();
                    rabbitTemplate.convertAndSend(msg);*/
                    //TODO:将查询的消息发送到队列中
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties messageProperties = message.getMessageProperties();
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,KillSuccessUserInfo.class);

                            return message;
                        }
                    });
                }
            }

        }catch (Exception e){
            LOGGER.error("异步发送消息失败{}",e.fillInStackTrace());
        }
    }

    /**
     * 秒杀成功后订单-入死信队列，在一定时间内失效超时未支付
     * @param orderCode
     */
    @Override
    public void sendKillSuccessOrderExpireMsg(String orderCode) {
        try {
            if (StringUtil.isNotBlank(orderCode)){
                KillSuccessUserInfo info = itemKillSuccessMapper.selectByCode(orderCode);
                if (info != null){
                    LOGGER.info("秒杀成功后生成抢购订单-入死信队列成功：{}",orderCode,info);
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties mp = message.getMessageProperties();
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,KillSuccessUserInfo.class);
                            //TODO:动态设置TTL
                            mp.setExpiration(env.getProperty("mq.kill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
            }
        }catch (Exception e){
            LOGGER.error("秒杀成功后生成抢购订单-入死信队列，发生异常：{}",orderCode,e.fillInStackTrace());
        }
    }
}
