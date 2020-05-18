package io.learn.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class GroupChatClient {
    private int port;
    private String host;

    public GroupChatClient(String host, int port){
        this.port=port;
        this.host=host;
    }

    public void chat(){
        NioEventLoopGroup workGroup=new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("clientHandler", new GroupChatHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                channel.writeAndFlush(Unpooled.copiedBuffer(s,CharsetUtil.UTF_8));
            }
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        GroupChatClient client=new GroupChatClient("127.0.0.1",8888);
        client.chat();
    }
}
