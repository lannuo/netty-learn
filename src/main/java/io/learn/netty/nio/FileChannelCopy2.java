package io.learn.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopy2 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("./hello2.txt");
        FileChannel readchannel = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("./hello3.txt");
        FileChannel writechannel = fileOutputStream.getChannel();
        writechannel.transferFrom(readchannel, 0, readchannel.size());
        readchannel.close();
        writechannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
