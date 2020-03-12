package com.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manage.base.until.Result;
import com.manage.dto.ExportDTO;
import com.manage.util.ExportExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description: 类作用描述
 * @Author: hhq
 * @CreateDate: 2020/3/12 15:29
 * @Version: 1.0
 */
@Controller
@RequestMapping("/ssm/export")
public class ExportController {
    /**
     * 执行状态：“success” or "fail"
     */
    private static final String RESULT = "result";
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    @RequestMapping(value = "/batchExport", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> batchNotNeedReceived(final HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        try {
            List<ExportDTO> list = ExportExcelUtil.parseExcel(request, ExportDTO.batchesHeader, ExportDTO.class);
            map.put(RESULT,FAIL);
            map.put("resData", JSONObject.toJSONString(list));
            return map;
        } catch (Exception e) {
            map.put(RESULT,FAIL);
            return map;
        }
    }
}
