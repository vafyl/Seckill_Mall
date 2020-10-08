package com.zh.server.service.impl;

import com.zh.model.entity.ItemKill;
import com.zh.model.mapper.ItemKillMapper;
import com.zh.server.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Space_Pig
 * @date 2020/10/01 8:46
 */
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    private ItemKillMapper itemKillMapper;

    @Override
    public List<ItemKill> getItemKill() {
        return itemKillMapper.selectAll();
    }

    @Override
    public ItemKill getItemKillInfo(Integer id) {
        return itemKillMapper.selectById(id);
    }
}
