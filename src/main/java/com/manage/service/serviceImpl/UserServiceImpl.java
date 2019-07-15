package com.manage.service.serviceImpl;

import com.manage.dao.UserMapper;
import com.manage.model.User;
import com.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2019/6/28 17:10
 * @Created by hhq
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id.intValue());
    }
}
