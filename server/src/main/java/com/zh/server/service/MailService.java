package com.zh.server.service;

import com.zh.server.dto.MailDto;

/**
 * @author Space_Pig
 * @date 2020/10/06 16:57
 */
public interface MailService {
    void sendSimpleEmail(MailDto mailDto);
    void sendHtmlEmail(MailDto mailDto);
}
