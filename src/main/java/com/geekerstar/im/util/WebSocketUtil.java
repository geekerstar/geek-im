package com.geekerstar.im.util;

import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * @author geekerstar
 * @date 2020/8/25 13:59
 * @description
 */
public class WebSocketUtil {
    /**
     * 获得Channel远程主机IP地址
     */
    public static String getChannelAddress(final Channel channel) {
        if (null == channel) {
            return "";
        }
        SocketAddress address = channel.remoteAddress();
        String addr = (address != null ? address.toString() : "");
        int index = addr.lastIndexOf("/");
        if (index >= 0) {
            return addr.substring(index + 1);
        }
        return addr;
    }
}
