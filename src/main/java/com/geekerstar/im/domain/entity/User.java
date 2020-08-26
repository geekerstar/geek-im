package com.geekerstar.im.domain.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author geekerstar
 * @date 2020/8/25 14:39
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User", description = "用户信息")
public class User {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("昵称")
    private String nick;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("头像地址")
    private String avatarAddress;

    @ApiModelProperty("确认在线时间")
    private long time = 0;
}
