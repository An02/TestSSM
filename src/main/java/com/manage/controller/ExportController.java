package com.manage.controller;

import base.model.ExcelImportModel;
import base.model.ExcelImportResult;
import com.alibaba.fastjson.JSONObject;
import com.manage.dto.ExportDTO;
import com.manage.util.ExportExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/3/12 15:29
 * @Version: 1.0
 */
@Controller
@RequestMapping("/ssm/export")
public class ExportController {
    private static final String FAIL = "fail";
    /**
     * 执行状态：“success” or "fail"
     */
    private static final String RESULT = "result";
    private static final String SUCCESS = "success";

    @RequestMapping(value = "/batchExport", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> batchNotNeedReceived(final HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        try {
            List<ExportDTO> list = ExportExcelUtil.parseExcel(request, ExportDTO.batchesHeader, ExportDTO.class);
            ExcelImportResult excelImportResult = new ExcelImportResult();
            List<String> errorString = new ArrayList<>();
            for (ExportDTO exportDTO : list) {
                exportDTO.setMsg("错误提示");
                errorString.add("第"+exportDTO.getRow() + "行" + exportDTO.getMsg()+";");
            }
            String errorInfoDetail = StringUtils.arrayToDelimitedString(errorString.toArray(), "<br/>");
            excelImportResult.setItems(list);
            excelImportResult.setDetailMsg(errorInfoDetail);
            map.put(RESULT, FAIL);
            map.put("resData", JSONObject.toJSONString(excelImportResult));
            return map;
        } catch (Exception e) {
            map.put(RESULT, FAIL);
            return map;
        }
    }
}
