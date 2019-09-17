package com.manage.model;

import com.manage.base.model.BaseInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hhq
 */
@Data
public class User extends BaseInfo implements Serializable {
    private static final long serialVersionUID = -1353749893029834936L;

    private Integer id;

    private String username;

    private String password;
}