package com.zh.model.mapper;

import com.zh.model.entity.ItemKill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemKillMapper {
    List<ItemKill> selectAll();

    ItemKill selectById(@Param("id") Integer id);

    int updateKillItem(@Param("killId") Integer killId);



    ItemKill selectByIdV2(@Param("id") Integer id);

    int updateKillItemV2(@Param("killId") Integer killId);
}