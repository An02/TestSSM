package com.manage.base.model;

import com.manage.annotation.NoRepeat;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hhq
 * @Classname BaseInfo
 * @Description TODO
 * @Date 2019/7/31 14:52
 * @Created by hhq
 */
@Data
public class BaseInfo implements Serializable {
    private static final long serialVersionUID = -1353749893026834936L;
    private Integer type;
    @NoRepeat(desc = "昵称")
    private String nickname;
    private String createrTime;
}
