package io.learn.netty;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIO {
    public static void main(String[] args) throws IOException {
        //创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(3333);
        System.out.println("服务器启动了");
        System.out.println("线程id： "+ Thread.currentThread().getId()+", name:"+ Thread.currentThread().getName());

        while (true){
            System.out.println("等待连接。。。");
            System.out.println("线程id： "+ Thread.currentThread().getId()+", name:"+ Thread.currentThread().getName());
            Socket socket = serverSocket.accept();
            System.out.println("连接一个客户端");
            System.out.println("线程id： "+ Thread.currentThread().getId()+", name:"+ Thread.currentThread().getName());
            executorService.submit(() -> {
                handler(socket);
            });
        }
    }
    private static void handler(Socket socket){
        try {
            System.out.println("线程id： "+ Thread.currentThread().getId()+", name:"+ Thread.currentThread().getName());
            byte[] bytes=new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true){
                System.out.println("线程id： "+ Thread.currentThread().getId()+", name:"+ Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭客户端连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
