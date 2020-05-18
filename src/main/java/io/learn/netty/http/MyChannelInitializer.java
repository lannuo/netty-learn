package io.learn.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch){
        ch.pipeline().addLast("myHttpServerCodec", new HttpServerCodec());
        ch.pipeline().addLast("myHttpHandler", new MyHttpHandler());
    }
}
