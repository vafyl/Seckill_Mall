package com.zh.server.service;

import com.zh.model.entity.KillSuccessUserInfo;

/**
 * @author Space_Pig
 * @date 2020/10/04 23:06
 */
public interface RabbitSenderService {
    void sendKillSuccessEmailMsg(String orderNo);
    void sendKillSuccessOrderExpireMsg(String orderCode);
}
