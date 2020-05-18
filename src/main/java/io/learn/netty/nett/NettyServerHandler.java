package io.learn.netty.nett;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ChannelHandlerContext : "+ ctx);
        ByteBuf byteBuf=(ByteBuf)msg;
        System.out.println("客户端发送的消息为："+ byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址为：" + ctx.channel().remoteAddress());
        System.out.println("客户端的hash为： "+ ctx.channel().hashCode());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～", CharsetUtil.UTF_8));
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(10 * 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("task普通任务",CharsetUtil.UTF_8));
        });
        ctx.channel().eventLoop().schedule(()->{
            try {
                Thread.sleep(5 * 1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("task定时任务",CharsetUtil.UTF_8));
        },5, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
