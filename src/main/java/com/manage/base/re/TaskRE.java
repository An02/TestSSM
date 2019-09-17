package com.manage.base.re;

import com.manage.base.model.BaseTaskInfo;
import lombok.Data;

/**
 * @Classname TaskRE
 * @Description TODO
 * @Date 2019/9/5 14:04
 * @author by hhq
 */
@Data
public class TaskRE extends BaseTaskInfo {
    /**
     * id
     */
    private Long Id;

    /**
     * 任务编号
     */
    private String taskNo;
}
