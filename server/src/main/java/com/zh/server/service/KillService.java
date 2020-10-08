package com.zh.server.service;

import com.zh.model.entity.ItemKill;

/**
 * @author Space_Pig
 * @date 2020/10/04 21:03
 */
public interface KillService {
    Boolean killItem(Integer id,Integer userID) throws Exception;
    Boolean killItemV2(Integer id,Integer userID) throws Exception;
    Boolean killItemV3(Integer id,Integer userID) throws Exception;
    Boolean killItemV4(Integer id,Integer userID) throws Exception;


    void commonRecordKillSuccessInfo(ItemKill kill, Integer userId) throws Exception;
}
