package com.manage.event.publish.impl;

import com.manage.event.publish.OrderPublishService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/13 15:36
 * @Version: 1.0
 */
@Service("orderPublishServiceImpl")
public class OrderPublishServiceImpl implements OrderPublishService<ApplicationEvent>, ApplicationContextAware {
   private ApplicationContext applicationContext;
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
