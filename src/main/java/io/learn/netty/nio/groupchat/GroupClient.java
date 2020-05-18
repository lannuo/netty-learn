package io.learn.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupClient {

    private Selector selector;
    private SocketChannel socketChannel;
    private final String HOST="127.0.0.1";
    private final int PORT=6667;
    private String name;

    public GroupClient(){
        try {
            selector = Selector.open();
            socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            name=socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("我是："+ name);
        }catch (IOException e){
            System.out.println("init 失败");
        }
    }

    private void write(String info){
        info=name+"说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(){
        try {
            int read = selector.select();
            if(read>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel=(SocketChannel)key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int read1 = channel.read(byteBuffer);
                        if(read1>0){
                            System.out.println(name+"收到消息："+ new String(byteBuffer.array()).trim());
                        }
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupClient client=new GroupClient();
        new Thread(() -> {
            while (true){
                client.read();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNextLine()){
            client.write(scanner.nextLine());
        }
    }
}
