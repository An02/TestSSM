package com.manage.thredT;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/8/5 13:43
 * @Version: 1.0
 */
public class ChangeNum {
    public static Object lock  = new Object();
    public volatile int index = 1;
    public volatile boolean isPrint = false;
    public static void main(String[] args) {
        ChangeNum changeNum = new ChangeNum();
        Thread st1 = new Thread(changeNum.new t1());
        Thread st2 = new Thread(changeNum.new t2());
        st1.start();
        st2.start();
    }

    class t1 implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 50; i ++) {
                synchronized(lock){
                    if (!isPrint) {
                        try{
                            lock.wait();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println("t1: " + index++);
                    isPrint = false;
                    lock.notifyAll();
                }
            }
        }
    }

    class t2 implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 50; i ++) {
                synchronized(lock){
                    if (isPrint) {
                        try{
                            lock.wait();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    System.out.println("t2: " + index++);
                    isPrint = true;
                    lock.notifyAll();
                }
            }
        }
    }

}
