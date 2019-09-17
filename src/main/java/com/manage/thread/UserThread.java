package com.manage.thread;

import com.manage.model.User;
import com.manage.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @Classname UserThread
 * @Description TODO
 * @Date 2019/8/21 14:07
 * @Autowired by hhq
 */
public class UserThread implements Runnable {
    private static Logger LOGGER = Logger.getLogger(UserThread.class);
    private Long userId;
    @Autowired
    private UserService userService;

    @Resource(name="redisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public void run() {
        System.out.println("UserThread 线程开始！！");
        if (this.userId == null) {
            System.out.println("userId 为空，UserThread 线程终止！！");
        }
        User user = userService.getUserById(this.userId);
        try {
            System.out.println("线程睡眠1秒");
            Thread.sleep(1000);
            redisTemplate.opsForValue().set(user.getUsername(),user);
        } catch (Exception e) {
            LOGGER.error("请求异常:  "+e.getMessage());
        }
    }

    public UserThread(Long userId) {
        this.userId = userId;
    }
}
