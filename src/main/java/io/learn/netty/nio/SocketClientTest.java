package io.learn.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

public class SocketClientTest {
    public static void main(String[] args) throws Exception{
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if(!channel.connect(inetSocketAddress)){
            while (!channel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞");
            }
        }

        String str="hello, world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        channel.write(buffer);
        System.in.read();


    }
}
