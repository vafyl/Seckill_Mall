package com.zh.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String user_name;

    private String password;

    private String phone;

    private String email;

    private Byte is_active;

    private Date createTime;

    private Date updateTime;
}