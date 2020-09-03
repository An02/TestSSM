package com.manage.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Byte;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/7/24 14:56
 * @Version: 1.0
 */
public class Servicenio {
    static byte[] msg = new byte[1024];
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(9091);
            while(true){
                System.out.println("wait conn.....");
                Socket clientSocket = serverSocket.accept();
                clientSocket.getInputStream().read(msg);
                System.out.println(new String(msg));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
