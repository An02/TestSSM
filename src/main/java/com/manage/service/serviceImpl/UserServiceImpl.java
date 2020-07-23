package com.manage.service.serviceImpl;

import com.manage.aspect.LogAnnotation;
import com.manage.dao.UserMapper;
import com.manage.model.User;
import com.manage.service.OrderManager;
import com.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2019/6/28 17:10
 * @author by hhq
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    OrderManager orderManager;

    @Override
    // @LogAnnotation
    public User getUserById(Long id) {
        System.out.println("进入service");
        User user = userMapper.selectByPrimaryKey(id.intValue());
        return user;
    }

}
