package com.manage.dto;

import base.model.ExcelImportModel;
import lombok.Data;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/3/12 15:34
 * @Version: 1.0
 */
@Data
public class ExportDTO extends ExcelImportModel {
    /**
     * 用户编号
     */
    public String userNo;

    /**
     *备注
     */
    public String remarks;

    /**
     * 行号
     */
    public int row;

    public static String[] batchesHeader = new String[]{
            "userNo&#用户编号",
            "remarks&#备注"
    };

}
