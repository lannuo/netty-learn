package io.learn.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopy {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("./hello.txt");
        FileChannel readchannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        FileOutputStream fileOutputStream = new FileOutputStream("./hello2.txt");
        FileChannel outChannel = fileOutputStream.getChannel();
        while (true){
            byteBuffer.clear();
            int read = readchannel.read(byteBuffer);
            System.out.println("read: "+ read);
            if(read==-1)break;
            byteBuffer.flip();
            outChannel.write(byteBuffer);
        }
        readchannel.close();
        outChannel.close();
    }
}
