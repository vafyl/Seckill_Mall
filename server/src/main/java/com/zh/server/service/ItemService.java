package com.zh.server.service;

import com.zh.model.entity.ItemKill;

import java.util.List;

/**
 * @author Space_Pig
 * @date 2020/10/01 8:42
 */
public interface ItemService {
    List<ItemKill> getItemKill();
    ItemKill getItemKillInfo(Integer id);
}
