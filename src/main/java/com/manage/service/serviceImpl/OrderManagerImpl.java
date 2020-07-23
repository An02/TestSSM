package com.manage.service.serviceImpl;

import com.manage.dto.OrderDTO;
import com.manage.event.order_event.OrderEvent;
import com.manage.event.publish.OrderPublishService;
import com.manage.model.User;
import com.manage.service.OrderManager;
import com.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2019/12/13 15:12
 * @Version: 1.0
 */
@Service("orderManager")
public class OrderManagerImpl implements OrderManager {
    @Autowired
    @Qualifier("orderPublishServiceImpl")
    OrderPublishService orderPublishService;

    @Autowired
    UserService userService;

    @Override
    public void create(User user) {
        String orderNo = "TH124551212";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNo(orderNo);
        orderDTO.setCreateTime(new Date());
        orderDTO.setUserId(user.getId());
        // 发布事件
        orderPublishService.publishEvent(new OrderEvent(this, orderDTO));
    }
}
