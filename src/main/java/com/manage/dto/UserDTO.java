package com.manage.dto;

import java.io.Serializable;

/**
 * @Classname UserDTO
 * @Description TODO
 * @Date 2019/6/28 11:09
 * @Created by hhq
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 655828798248467207L;
    /**
     * 用户ID
     */
    private Long id;

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
