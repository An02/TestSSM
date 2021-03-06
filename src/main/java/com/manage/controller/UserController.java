package com.manage.controller;

import com.manage.base.until.Result;
import com.manage.dto.UserDTO;
import com.manage.model.User;
import com.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.*;


/**
 * @Classname UserController
 * @Description 用户控制类
 * @Date 2019/6/25 16:27
 * @Created by hhj
 */

@Controller
@RequestMapping("/ssm/user")
public class UserController {
    @Autowired
    UserService userService;

    @Resource(name="redisTemplate")
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Result setUser(UserDTO user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(12L);
        System.out.println("============" + user.getId());
        User user1 = userService.getUserById(user.getId());
        try {
            redisTemplate.opsForValue().set(user1.getUsername(),"usr");
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("往redis set 值异常！！！");
        }
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    @ResponseBody
    public Result getUser(UserDTO user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(12L);
        System.out.println("============" + user.getId());
        User user1 = userService.getUserById(user.getId());
        try {
            String user2 = (String) redisTemplate.opsForValue().get(user1.getUsername());
            return Result.success(user2);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取redis 值异常！！！");
        }
    }

    public void testFeature() {
        //创建Executor- Service，通 过它你可以 向线程池提 交任务
        ExecutorService executor = Executors.newCachedThreadPool();
        //向Executor- Service提交一个 Callable对象
        final Future<User> futureRate = executor.submit(new Callable<User>() {
            @Override
            public User call() {
                //以异步方式在新的线程中执行耗时的操作
                System.out.println("===异步之内===");
                return userService.getUserById(1L);
            }
        });
        //异步操作进行的同时你可以做其他的事情
        System.out.println("===异步之外===");

        try {
            //获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待1秒钟之后退出
            User result = futureRate.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            // 计算抛出一个异常
            e.printStackTrace();
        } catch (InterruptedException ie) { // 当前线程在等待过程中被中断
        } catch (TimeoutException te) { // 在Future对象完成之前超过已过期
        }
    }
}