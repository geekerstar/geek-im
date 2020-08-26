package com.geekerstar.im.runner;

import com.geekerstar.im.constant.WebSocketConstant;
import com.geekerstar.im.handler.WebSocketChannelHandler;
import com.geekerstar.im.service.WebSocketInfoService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author geekerstar
 * @date 2020/8/25 15:20
 * @description
 */
@Slf4j
@Component
public class WebsocketRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new WebSocketChannelHandler());
            log.info("服务端开启，等待客户端连接....");
            Channel ch = b.bind(WebSocketConstant.WEB_SOCKET_PORT).sync().channel();
            //创建一个定长线程池，支持定时及周期性任务执行
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            WebSocketInfoService webSocketInfoService = new WebSocketInfoService();
            //定时任务:扫描所有的Channel，关闭失效的Channel
            executorService.scheduleAtFixedRate(webSocketInfoService::scanNotActiveChannel, 3, 60, TimeUnit.SECONDS);
            //定时任务:向所有客户端发送Ping消息
            executorService.scheduleAtFixedRate(webSocketInfoService::sendPing, 3, 50, TimeUnit.SECONDS);
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("服务端退出，关闭客户端连接....");
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
