package com.manage.proxy;

import com.manage.service.UserService;
import com.manage.util.SpringConfigTool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.lang.reflect.Proxy;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/19 14:16
 * @Version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
        UserService userService1 = (UserService) ac.getBean("userService");
        //创建中介类实例
        DynamicProxy inter = new DynamicProxy(new Vendor());
        //加上这句将会产生一个$Proxy0.class文件，这个文件即为动态生成的代理类文件
        System.getProperties().put("com.manage.proxy.saveGeneratedFiles","true");

        //获取代理类实例sell
        Sell sell = (Sell)inter.getProxy();
        //通过代理类对象调用代理类方法，实际上会转到invoke方法调用
        sell.sell();
        sell.ad();
    }
}
