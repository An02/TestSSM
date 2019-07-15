package com.manage.base.until;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname Result
 * @Description TODO
 * @Date 2019/7/12 14:10
 * @Created by hhq
 */
@Data
public class Result<T> implements Serializable {
    /**
     * 状态
     */
    private int status;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T re;

    public Result(int status, boolean success, String code, String msg) {
        this.status = status;
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public Result(int status, boolean success, String code) {
        this.status = status;
        this.success = success;
        this.code = code;
    }

    public Result(int status, boolean success, String code, String msg, T re) {
        this.status = status;
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.re = re;
    }

    public static <T> Result<T> error(T data) {
        return new Result(2,Boolean.FALSE,"2","flase",data);
    }
    public static Result error() {
        return new Result(2,Boolean.FALSE,"2","flase");
    }
    public static Result error(String msg) {
        return new Result(2,Boolean.FALSE,"2",msg);
    }
    public static <T> Result<T> success(T data){
        return new Result(1,Boolean.TRUE,"1","true",data);
    }

    public static Result success(){
        return new Result(1,Boolean.TRUE,"1","true");
    }
}
