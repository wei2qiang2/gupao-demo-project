package com.demo.jvm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Netty服务端处理器
 *
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    //收到消息时，返回消息
    List<byte []> datas = new ArrayList<byte[]>();

    static {
        System.err.println("Netty Server Hnadler");
    }
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
        System.out.println(123);
        ByteBuf heapBuffer = null;
        try{
            //收到消息时直接打印输出
            System.out.println("服务端接收的消息： " +  o);
            if ("quit".equals(o.toString())){
                channelHandlerContext.close();
            }
            byte[] data = new byte[1024 * 1024];
            //datas.add(data)
            heapBuffer = channelHandlerContext.alloc().heapBuffer();
            heapBuffer.writeBytes(data);
            //返回客户端消息
            channelHandlerContext.channel().writeAndFlush(heapBuffer + "\n");
        }finally {
//            heapBuffer.release();
        }
    }
}
