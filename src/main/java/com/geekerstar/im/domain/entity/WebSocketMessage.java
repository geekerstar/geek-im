package com.geekerstar.im.domain.entity;

import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author geekerstar
 * @date 2020/8/25 14:39
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "WebSocketMessage", description = "websocket消息")
public class WebSocketMessage {

    @ApiModelProperty("码值")
    private Integer code;

    @ApiModelProperty("发送人信息")
    private User user;

    @ApiModelProperty("接收人 id")
    private String receiverId;

    @ApiModelProperty("在线时间")
    private String time;

    @ApiModelProperty("消息内容")
    private String message;

    /**
     * 可以存放在线人数，在线用户列表，code等
     */
    private Map<String, Object> body = Maps.newHashMap();

    public WebSocketMessage(Integer code, String message, String time) {
        this.code = code;
        this.time = time;
        this.message = message;
    }

    public WebSocketMessage(Integer code, String message, User user,
                            String receiverId, String time) {
        this.code = code;
        this.user = user;
        this.receiverId = receiverId;
        this.time = time;
        this.message = message;
    }
}
