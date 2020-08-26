package com.geekerstar.im.service;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.geekerstar.im.domain.entity.User;
import com.geekerstar.im.domain.entity.WebSocketMessage;

import java.util.Map;

/**
 * @author geekerstar
 * @date 2020/8/25 14:40
 * @description
 */
public class MessageService {

    public String messageJsonStringFactory(int messageCode, String chatMessage, int systemMessageCode, Object o) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(messageCode, chatMessage, DateUtil.now());
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("systemMessageCode", systemMessageCode);
        map.put("object", o);
        webSocketMessage.setBody(map);
        return new Gson().toJson(webSocketMessage);
    }

    public String messageJsonStringFactory(int messageCode, String chatMessage, User user, String receiverId) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(messageCode, chatMessage, user, receiverId, DateUtil.now());
        return new Gson().toJson(webSocketMessage);
    }
}
