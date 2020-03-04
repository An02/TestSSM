package com.manage.proxy;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/19 14:14
 * @Version: 1.0
 */
public class Vendor implements Sell {
    public void sell() {
        System.out.println("In sell method");
    }

    public void ad() {
        System.out.println("ad method");
    }
}
