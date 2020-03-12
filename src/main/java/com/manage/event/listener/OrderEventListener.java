package com.manage.event.listener;

import com.manage.dto.OrderDTO;
import com.manage.event.order_event.OrderEvent;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/13 15:43
 * @Version: 1.0
 */
@Component
public class OrderEventListener implements SmartApplicationListener {
    /**
     * 支持的事件类型
     * @param aClass
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        boolean r = aClass == OrderEvent.class;
        System.out.println("是否处理事件：" + r);
        return r;
    }

    /**
     * 支持的事件源
     * @param aClass
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        OrderEvent orderEvent = (OrderEvent)applicationEvent;
        OrderDTO orderDTO = orderEvent.getOrder();
        try {
            System.out.println("======订单处理中====");
            throw new Exception("fsdfs");
        } catch (Exception e) {
            System.out.println("======订单处理异常====：");
            System.out.println(e.getMessage());
            throw new Exception("fsdfs");
        }
        // 收到事件可以处理业务逻辑
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
