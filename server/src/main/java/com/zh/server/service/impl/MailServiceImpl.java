package com.zh.server.service.impl;

import com.zh.server.dto.MailDto;
import com.zh.server.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author Space_Pig
 * @date 2020/10/06 16:59
 */
@Service
@EnableAsync(proxyTargetClass = true)
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送秒杀成功邮箱信息
     * @param mailDto
     */
    @Async
    @Override
    public void sendSimpleEmail(MailDto mailDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty("mail.send.from"));
            message.setTo(mailDto.getTos());
            message.setSubject(mailDto.getSubject());
            message.setText(mailDto.getContent());
            mailSender.send(message);
            LOGGER.info("发送秒杀简易邮箱信成功：",message);
        }catch (Exception e){
            LOGGER.error("发送秒杀简易邮箱信失败：",e.fillInStackTrace());
        }
    }

    /**
     * 发送HTML类型的邮箱内容
     * @param mailDto
     */
    @Async
    @Override
    public void sendHtmlEmail(MailDto mailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true,"utf-8");
            messageHelper.setFrom(env.getProperty("mail.send.from"));
            messageHelper.setText(mailDto.getContent(),true);
            messageHelper.setSubject(mailDto.getSubject());
            messageHelper.setTo(mailDto.getTos());
            mailSender.send(mimeMessage);
            LOGGER.info("发送秒杀HTML邮箱信息成功");
        }catch (Exception e){
            LOGGER.error("发送秒杀HTML邮箱信息失败",e.fillInStackTrace());
        }
    }


}
