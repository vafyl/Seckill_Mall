package com.zh.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {

    private String user_name;
    private String phone;
    private String email;
    private String itemName;

    @Override
    public String toString() {
        return  super.toString()+"KillSuccessUserInfo{" +
                "userName='" + user_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
