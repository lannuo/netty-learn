package io.learn.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class GroupChatServer {
    private int port;

    public GroupChatServer(int port){
        this.port=port;
    }

    public void run(){
        NioEventLoopGroup bossGroup=new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            p.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            p.addLast("groupChatServerHandler", new GroupChatServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("server 启动成功～");
            channelFuture.channel().closeFuture().sync();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatServer(8888).run();
    }

}
