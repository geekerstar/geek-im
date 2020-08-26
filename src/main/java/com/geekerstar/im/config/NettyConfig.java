package com.geekerstar.im.config;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author geekerstar
 * @date 2020/8/25 15:13
 * @description
 */
public class NettyConfig {
    /**
     * 每个客户端建立连接后存放一个Channel对象，用于使用 writeAndFlush 方法广播信息
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
