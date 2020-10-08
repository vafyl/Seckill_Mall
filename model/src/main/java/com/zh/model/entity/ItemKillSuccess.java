package com.zh.model.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ItemKillSuccess {
    private String code;

    private Integer item_id;

    private Integer kill_id;

    private String user_id;

    private Byte status;

    private Date create_time;

    private Integer diffTime;

}