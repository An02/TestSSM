package com.manage.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/7/7 17:48
 * @Version: 1.0
 */
public class SpringConfigTool implements ApplicationContextAware{//extends ApplicationObjectSupport{

    private static ApplicationContext context = null;
    private static SpringConfigTool stools = null;
    public synchronized static SpringConfigTool init(){
        if(stools == null){
            stools = new SpringConfigTool();
        }
        return stools;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext(){
        return context;
    }

}
