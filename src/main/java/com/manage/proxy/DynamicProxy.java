package com.manage.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/19 14:15
 * @Version: 1.0
 */
public class DynamicProxy implements InvocationHandler {
    /**
     * obj为委托类对象
     */
    private Object obj;

    public DynamicProxy(Object obj) {
        super();
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object result = method.invoke(obj, args);
        System.out.println("after");
        return result;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), obj.getClass().getInterfaces(), this);
    }
}
