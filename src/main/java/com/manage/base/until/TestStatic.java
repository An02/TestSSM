package com.manage.base.until;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @Classname TestStatic
 * @Description TODO
 * @Date 2019/7/11 11:11
 * @Created by hhq
 */
public class TestStatic {
    public static  <E> E setPropert(E model) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.setProperty(model,"titel","hjhj");
        return model;
    }
}
