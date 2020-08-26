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

    /**
     * 群聊消息
     *
     * @param messageCode
     * @param chatMessage
     * @param systemMessageCode
     * @param object
     * @return
     */
    public String messageJsonStringFactory(int messageCode, String chatMessage, int systemMessageCode, Object object) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(messageCode, chatMessage, DateUtil.now());
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("systemMessageCode", systemMessageCode);
        map.put("object", object);
        webSocketMessage.setBody(map);
        return new Gson().toJson(webSocketMessage);
    }

    /**
     * 私聊消息
     *
     * @param messageCode
     * @param chatMessage
     * @param user
     * @param receiverId
     * @return
     */
    public String messageJsonStringFactory(int messageCode, String chatMessage, User user, String receiverId) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(messageCode, chatMessage, user, receiverId, DateUtil.now());
        return new Gson().toJson(webSocketMessage);
    }
}
