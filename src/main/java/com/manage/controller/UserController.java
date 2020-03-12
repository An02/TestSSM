package com.manage.controller;

import com.manage.base.until.Result;
import com.manage.dto.UserDTO;
import com.manage.model.User;
import com.manage.service.OrderManager;
import com.manage.service.UserService;
import com.manage.thread.UserThread;
import com.manage.util.Excel2007Util;
import com.manage.util.ExportUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Resource(name="redisTemplate")
    private RedisTemplate redisTemplate;
    @Autowired
    OrderManager orderManager;

    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Result setUser(UserDTO user) {
        LOGGER.info("请求进入");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        System.out.println("============" + user.getId());
        Thread userTread = new Thread(new UserThread(userDTO.getId()),"用户线程");
        User user1 = userService.getUserById(user.getId());
        if (user1==null) {
            System.out.println("结束");
            return Result.error("User 信息为空，userId:"+ user.getId());
        }
        userTread.start();
        try {
            redisTemplate.opsForValue().set(user1.getUsername(),"usr");
            System.out.println("结束");
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("请求异常:  "+e.getMessage());
            return Result.error("往redis set 值异常！！！");
        }
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    @ResponseBody
    public Result getUser(UserDTO user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
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

    @RequestMapping(value = "/testEvent", method = RequestMethod.POST)
    @ResponseBody
    public Result testEvent(@RequestBody UserDTO user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        System.out.println("============" + user.getId());
        try {
            User user1 = userService.getUserById(user.getId());
            orderManager.create(user1);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建订单异常！！！");
        }
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView export(HttpServletRequest request, HttpServletResponse response) {
        String[] columnArr = new String[]{"userNo&#用户编号", "remarks&#备注"};
        try {
            write(response, columnArr, "导出文件模板");
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * description: 生成并输出导入模版excel文件
     *
     * @param response
     * @param columnArr
     * @param fileName
     * @return void
     * @author tianhua.xie（tianhua.xie@ucarinc.com）
     * @date 2019-02-28 17:27:51
     **/
    private static void write(HttpServletResponse response, String[] columnArr, String fileName) throws IOException {
        Excel2007Util excelUtil = new Excel2007Util();

        List<Map<String, String>> listData = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("frameNo","测试借用单");
        listData.add(map);
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = null;
        int len;
        byte[] bytes = new byte[1024];

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");

        try {
            inputStream = excelUtil.exportForListPage(columnArr, listData);
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            response.flushBuffer();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
