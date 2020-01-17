package com.demo.jvm.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private static String host = "127.0.0.1";
    private static int port = 6789;
    //通过NIO的方式来接收连接和处理连接
    private static EventLoopGroup group = new NioEventLoopGroup();
    private static Bootstrap b = new Bootstrap();
    private static Channel ch;

    /**
     * Netty创建的全部都是AbstracBootstarp
     * 客户端是Bootstrap
     * 服务端则是ServerBootstrap
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("客户端启动成功......");
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.handler(new NettyClientFilter());
        //连接服务端
        ch = b.connect(host, port).sync().channel();
        start();
    }

    private static void start(){
        for (int i = 0; i < 1000000000; i++) {
            String str = "Hello Netty";
            ch.writeAndFlush(str +"\r\n");
            System.out.println("客户端发送数据：" + str);
        }
    }
}
