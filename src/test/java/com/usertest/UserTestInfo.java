package com.usertest;

import com.manage.dto.UserDTO;
import com.manage.model.User;
import com.manage.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/7/6 17:35
 * @Version: 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-*.xml")
public class UserTestInfo {

    @Autowired
    private ApplicationContext ac;

    @Test
    public void test0() {
        System.out.println("测试用");
    }

    @Test
    public void testSelect() {
        URL path = UserTestInfo.class.getResource( "dubbo/dubbo.xml");
        ModelAndView mav = new ModelAndView("redirect:/user/list");
        //ApplicationContext a = new ClassPathXmlApplicationContext("spring/applicationContext-tx.xml,spring/applicationContext-redis.xml,spring/applicationContext-dao.xml,spring/applicationContext-service.xml");
        // AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        UserService userService1 = (UserService) ac.getBean("userService");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        User user1 = userService1.getUserById(userDTO.getId());
        System.out.println("测试");
    }

    @Test
    public  void  testStram() {
        try{
            InputStream os = new DataInputStream(System.in);
            byte[] b = new byte[100];
            while (true) {
                os.read(b);
                if (b.length > 10) {
                    break;
                }
            }
            System.out.println(b.length);
        }catch (IOException e){
           e.printStackTrace();
        }

    }
}
