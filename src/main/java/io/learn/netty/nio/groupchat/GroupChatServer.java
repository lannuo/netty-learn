package io.learn.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static int port=6667;

    public GroupChatServer(){
        try{
            selector=Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            System.out.println("init 失败");
        }
    }
    public void listen(){
        try {
            while (true) {
                int i = selector.select();
                if(i>0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel channel = serverSocketChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector,SelectionKey.OP_READ);
                            System.out.println(channel.getRemoteAddress()+"连接上线了");
                        }
                        if(key.isReadable()){
                            read(key);
                        }
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待连接...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println();
        }
    }

    private void read(SelectionKey key){
        SocketChannel socketChannel=null;
        try{
            socketChannel=(SocketChannel)key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(byteBuffer);
            if(read>0){
                String msg=new String(byteBuffer.array());
                System.out.println("服务器收到消息："+ socketChannel.getRemoteAddress() +": "+ msg.trim());
                redirct(msg, socketChannel);
            }
        }catch (IOException e){

            try {
                System.out.println(socketChannel.getRemoteAddress() +" 离线了");
                key.cancel();
                socketChannel.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void redirct(String msg, SocketChannel origin) throws IOException{
        System.out.println("服务器转发消息...");
        Set<SelectionKey> keys = selector.keys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            Channel channel=key.channel();
            if(channel instanceof SocketChannel && channel!=origin ){
                ((SocketChannel) channel).write(ByteBuffer.wrap(msg.getBytes()));
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server=new GroupChatServer();
        server.listen();
    }

}
