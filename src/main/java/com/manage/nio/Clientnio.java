package com.manage.nio;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/7/24 14:56
 * @Version: 1.0
 */
public class Clientnio {

    public static void main(String[] args) {
        try{
            Socket socket = new Socket("127.0.0.1",9091);
            while(true){
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                socket.getOutputStream().write(msg.getBytes());
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
