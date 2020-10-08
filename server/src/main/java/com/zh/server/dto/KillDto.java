package com.zh.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author Space_Pig
 * @date 2020/10/05 16:20
 */
@Data
@ToString
public class KillDto {

    @NotNull
    private Integer killId;
    private Integer userId;
}
