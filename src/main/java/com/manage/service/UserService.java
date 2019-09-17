package com.manage.service;

import com.manage.model.User;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2019/6/28 17:05
 * @author by hhj
 */
public interface UserService {

    /**
     * 通过id获取用户信息
     * @param id
     * @return user
     */
    public User getUserById(Long id);
}
