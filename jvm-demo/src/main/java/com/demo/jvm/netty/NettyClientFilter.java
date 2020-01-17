package com.demo.jvm.netty;

import io.netty.channel.*;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClientFilter extends ChannelInitializer {

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        //以（“\n”）为结尾分割的解码器
//        ph.addLast("framer", new DelimiterBasedFrameDecoder(81));
        //解码编码，应和客户端一致
        ph.addLast("decoder", new StringDecoder());
        ph.addLast("encoder", new StringEncoder());
        ph.addLast("handler", new NettyClientHandler());//客户端业务逻辑
    }
}
