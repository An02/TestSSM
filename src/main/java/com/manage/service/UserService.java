package com.manage.service;

import com.manage.model.User;
import org.springframework.stereotype.Service;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2019/6/28 17:05
 * @Created by hhj
 */
public interface UserService {

    public User getUserById(Long id);
}
