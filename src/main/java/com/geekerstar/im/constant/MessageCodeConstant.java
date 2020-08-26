package com.geekerstar.im.constant;

/**
 * @author geekerstar
 * @date 2020/8/25 14:17
 * @description
 */
public class MessageCodeConstant {

    /**
     * 登录
     */
    public static final int CLIENT_LOGIN_CODE = 1000;
    /**
     * 群聊
     */
    public static final int CLIENT_GROUP_CHAT_CODE = 1002;
    /**
     * 私聊
     */
    public static final int CLIENT_PRIVATE_CHAT_CODE = 1003;
    /**
     * pong 信息
     */
    public static final int CLIENT_PONG_CHAT_CODE = 1004;

    /**
     * 群聊信息
     */
    public static final int SERVER_GROUP_CHAT_MESSAGE_CODE = 2000;
    /**
     * 系统信息
     */
    public static final int SERVER_SYSTEM_MESSAGE_CODE = 2001;
    /**
     * 私聊信息
     */
    public static final int SERVER_PRIVATE_CHAT_MESSAGE_CODE = 2002;
    /**
     * ping 信息
     */
    public static final int SERVER_PING_MESSAGE_CODE = 2003;

    /**
     * 普通系统信息：用户上线，下线广播通知等
     */
    public static final int SYSTEM_NORMAL_MESSAGE_CODE = 3000;
    /**
     * 更新当前用户数量的系统信息
     */
    public static final int SYSTEM_UPDATE_USER_COUNT_MESSAGE_CODE = 3001;
    /**
     * 更新当前用户列表的系统信息
     */
    public static final int SYSTEM_UPDATE_USER_LIST_MESSAGE_CODE = 3002;
    /**
     * 获取个人信息的系统信息
     */
    public static final int SYSTEM_PERSONAL_MESSAGE_CODE = 3003;
}
