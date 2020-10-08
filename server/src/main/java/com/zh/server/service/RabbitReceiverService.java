package com.zh.server.service;

import com.zh.model.entity.KillSuccessUserInfo;


/**
 * @author Space_Pig
 * @date 2020/10/06 11:15
 */
public interface RabbitReceiverService {

    void consumerEmailMsg(KillSuccessUserInfo info);
    void consumerExpireOrder(KillSuccessUserInfo info);

}
