package com.manage.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/7/24 15:43
 * @Version: 1.0
 */
public class NioServer {
    static ByteBuffer msgBuffer = ByteBuffer.allocate(1024);
    static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) {
        try{
            ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress("127.0.0.1",9091);
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);
            while(true){
                for(SocketChannel channel : channelList){
                    if (channel == null) {
                        continue;
                    }
                    int read = channel.read(msgBuffer);
                    if (read > 0) {
                        System.out.println("rev msg : "+read);
                        msgBuffer.flip();
                        byte[] bs = new byte[read];
                        msgBuffer.get(bs);
                        String msg = new String(bs);
                        System.out.println("rev msg: "+msg);
                        msgBuffer.flip();
                    } else if (read == -1) {
                        channelList.remove(channel);
                    }
                }
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    channelList.add(channel);
                    channel.configureBlocking(false);
                    System.out.println("conn size: "+channelList.size());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
