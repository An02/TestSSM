package com.manage.dao;

import com.manage.model.User;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}