package com.geekerstar.im.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.geekerstar.im.config.NettyConfig;
import com.geekerstar.im.domain.entity.User;
import com.geekerstar.im.domain.entity.WebSocketMessage;
import com.geekerstar.im.util.WebSocketUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.geekerstar.im.constant.MessageCodeConstant.*;

/**
 * @author geekerstar
 * @date 2020/8/25 14:54
 * @description
 */
public class WebSocketInfoService {

    /**
     * 存储 Channel 与用户信息
     */
    public static ConcurrentMap<Channel, User> webSocketInfoMap = Maps.newConcurrentMap();
    /**
     * 用户在线数量
     */
    private static final AtomicInteger USER_COUNT = new AtomicInteger(0);

    /**
     * 新的客户端与服务端进行连接，先保存新的 channel，当连接建立后，客户端会发送用户登录请求（LOGIN_CODE），这时再将用户信息保存进去
     *
     * @param channel
     */
    public void addChannel(Channel channel) {
        User user = new User();
        user.setAddress(WebSocketUtil.getChannelAddress(channel));
        webSocketInfoMap.put(channel, user);
        NettyConfig.channelGroup.add(channel);
    }

    /**
     * 有用户退出，需要删除该用户的信息，并移除该用户的 channel
     * @param channel
     */
    public void deleteChannel(Channel channel) {
        String nick = webSocketInfoMap.get(channel).getNick();
        webSocketInfoMap.remove(channel);
        USER_COUNT.decrementAndGet();
        NettyConfig.channelGroup.remove(channel);
        //广播用户离开的信息
        TextWebSocketFrame tws = new TextWebSocketFrame(new MessageService().messageJsonStringFactory(SERVER_SYSTEM_MESSAGE_CODE, nick + "离开了聊天室~", SYSTEM_NORMAL_MESSAGE_CODE, null));
        new WebSocketInfoService().updateUserListAndCount();
        NettyConfig.channelGroup.writeAndFlush(tws);
    }

    /**
     * 向服务端发送信息，携带新的在线人数/携带新的用户列表
     */
    public void updateUserListAndCount() {
        //更新在线人数
        TextWebSocketFrame tws = new TextWebSocketFrame(new MessageService().messageJsonStringFactory(SERVER_SYSTEM_MESSAGE_CODE,
                null, SYSTEM_UPDATE_USER_COUNT_MESSAGE_CODE, USER_COUNT));
        NettyConfig.channelGroup.writeAndFlush(tws);

        //更新在线用户列表
        List<User> userList = new ArrayList<>();
        Set<Channel> set = webSocketInfoMap.keySet();
        for (Channel channel : set) {
            User user = webSocketInfoMap.get(channel);
            userList.add(user);
        }
        tws = new TextWebSocketFrame(new MessageService().messageJsonStringFactory(SERVER_SYSTEM_MESSAGE_CODE,
                null, SYSTEM_UPDATE_USER_LIST_MESSAGE_CODE, userList));
        NettyConfig.channelGroup.writeAndFlush(tws);
    }

    /**
     * 将 nick，id,avatarAddress 等用户信息保存到对应的 channel 的 value 中
     *
     * @param channel 属于某用户的 channel
     * @param nick    昵称
     * @param id      用户 id
     * @return 如果当前用户不存在，则返回 false
     */
    public boolean addUser(Channel channel, String nick, String id) {
        User user = webSocketInfoMap.get(channel);
        if (user == null) {
            return false;
        }
        user.setId(id);
        user.setNick(nick);
        user.setAvatarAddress(getRandomAvatar());
        user.setTime(System.currentTimeMillis());
        //用户在线数量 + 1
        USER_COUNT.incrementAndGet();
        return true;
    }

    /**
     * 返回一个随机的头像地址
     */
    private String getRandomAvatar() {
        int num = new Random().nextInt(33) + 1;
        return "../img/" + num + ".png";
    }

    /**
     * 发送私聊信息
     *
     * @param id  收信人id
     * @param tws
     */
    public void sendPrivateChatMessage(String id, TextWebSocketFrame tws) {
        Set<Channel> set = webSocketInfoMap.keySet();
        Channel receiverChannel = null;
        for (Channel channel : set) {
            User user = webSocketInfoMap.get(channel);
            if (user.getId().equals(id)) {
                receiverChannel = channel;
                break;
            }
        }
        if (receiverChannel != null) {
            receiverChannel.writeAndFlush(tws);
        }
    }

    /**
     * 广播 ping 信息
     */
    public void sendPing() {
        Set<Channel> keySet = webSocketInfoMap.keySet();
        for (Channel channel : keySet) {
            User user = webSocketInfoMap.get(channel);
            if (user == null) {
                continue;
            }
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setCode(SERVER_PING_MESSAGE_CODE);
            String message = new Gson().toJson(webSocketMessage);
            TextWebSocketFrame tws = new TextWebSocketFrame(message);
            NettyConfig.channelGroup.writeAndFlush(tws);
        }
    }

    /**
     * 从缓存中移除Channel，并且关闭Channel
     */
    public void scanNotActiveChannel() {
        Set<Channel> keySet = webSocketInfoMap.keySet();
        for (Channel channel : keySet) {
            User user = webSocketInfoMap.get(channel);
            if (user == null) {
                continue;
            }
            if (!channel.isOpen() || !channel.isActive() && (System.currentTimeMillis() - user.getTime()) > 10000) {
                deleteChannel(channel);
            }
        }
    }

    /**
     * 重设验证在线时间
     */
    public void resetUserTime(Channel channel) {
        User user = webSocketInfoMap.get(channel);
        if (user != null) {
            user.setTime(System.currentTimeMillis());
        }
    }
}
