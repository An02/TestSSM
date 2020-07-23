package com.manage.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Classname UserDTO
 * @Description TODO
 * @Date 2019/6/28 11:09
 * @Created by hhq
 */
@Component("userDTO")
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 655828798248467207L;
    /**
     * 用户ID
     */
    private Long id;

    @Autowired
    OrderDTO orderDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
