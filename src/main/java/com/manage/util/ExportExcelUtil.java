package com.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO
 *
 * @author baichuan.wu
 * @version 1.0.0
 * @date 2020-02-08 13:34
 */
public class ExportExcelUtil {

    /**
     * 2003- 版本的excel
     */
    private final static String EXCEL_2003L = ".xls";

    private final static String ROW = "row";
    /**
     * 2007+ 版本的excel
     */
    private final static String EXCEL_2007U = ".xlsx";
    private static final Integer MAX_ROWS = 10001;

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr,fileName
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws IOException {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (EXCEL_2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);
        } else if (EXCEL_2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new IllegalArgumentException("文件类型不合法");
        }
        return wb;
    }

    /**
     * @param response
     * @param exportMaps      Map<String, String> 为业务对象的属性-值对
     * @param excelName       形如xxx.xls
     * @param exportColumnArr 表头 形如： 属性名&#车架号
     * @return void    返回类型
     * @throws
     * @Title: exportDataToExcel
     * @Description: excel导出
     */
    public static void exportDataToExcel(HttpServletResponse response,
                                         List<Map<String, String>> exportMaps, String excelName, String[] exportColumnArr) throws IOException {
        ExcelUtil excelUtil = new ExcelUtil();
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(excelName + ".xls", "UTF-8"));
            inputStream = excelUtil.exportForListPage(exportColumnArr, exportMaps);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            response.flushBuffer();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String str = "";
        if (cell == null) {
            return str;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                NumberFormat nf = new DecimalFormat("#.##");
                double nbvalue = cell.getNumericCellValue();
                str = String.valueOf(nf.format(nbvalue));
                //读取日期
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    double d = cell.getNumericCellValue();
                    Date date = HSSFDateUtil.getJavaDate(d);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    str = sdf.format(date);
                }
                break;

            case Cell.CELL_TYPE_STRING:
                if (cell.getStringCellValue() != null) {
                    str = cell.getStringCellValue().trim();
                }
                break;

            default:
        }

        return str;
    }

    /**
     * 将流中的Excel数据转成List<Map>
     *
     * @param request 输入流
     * @param request 文件名（判断Excel版本）
     * @param maps    形如new String[]{ "invoiceAmount&#需开票金额",
     *                "creator&#新建人",
     *                "createTime&#新建时间"   }
     *                字段名称映射
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static <T> List<T> parseExcel(HttpServletRequest request,
                                         String[] maps, Class<T> clazz) throws IOException {

        Map<String, String> mapping = new HashMap<>(16);
        for (String s : maps) {
            String[] sarry = s.split("&#");
            mapping.put(sarry[1], sarry[0]);
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Collection<List<MultipartFile>> value = multipartRequest.getMultiFileMap().values();

        List<MultipartFile> list = new ArrayList<MultipartFile>();
        for (List<MultipartFile> temp : value) {
            if (!temp.isEmpty()) {
                list.addAll(temp);
            }
        }
        MultipartFile file = list.get(0);
        InputStream in = file.getInputStream();
        // 根据文件名来创建Excel工作薄
        List<T> lt = new ArrayList<T>();
        Workbook work = getWorkbook(in, file.getOriginalFilename());
        if (null == work) {
            return lt;
        }

        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        // 返回数据
        List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
        boolean flag = isExistField(ROW,clazz);
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            int rowss = sheet.getPhysicalNumberOfRows();
            if (rowss < 2) {
                return lt;

            }

            // 取第一行标题
            row = sheet.getRow(0);
            String[] title;
            if (row != null) {
                title = new String[row.getLastCellNum()];

                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    title[y] = getCellValue(cell);
                }

            } else {

                continue;
            }
            int rows = sheet.getPhysicalNumberOfRows();

            // 一次最多读取MaxRows行
            if (rows > MAX_ROWS + 1) {
                rows = MAX_ROWS + 1;
            }
            // 遍历当前sheet中的所有行
            int size = mapping.size();
            for (int j = 1; j < rows; j++) {
                row = sheet.getRow(j);
                Map<String, String> m = new HashMap<>(16);
                if (checkRowNull(row, size)) {
                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        // 判断是否为空行.空列
                        if (y < size) {
                            cell = row.getCell(y);
                            String key = title[y];
                            if (StringUtils.isEmpty(getCellValue(cell))) {
                                continue;
                            }
                            m.put(mapping.get(key), getCellValue(cell));
                        }
                    }
                    if (flag) {
                        m.put(ROW, String.valueOf(row.getRowNum() + 1));
                    }
                    // 遍历所有的列
                    ls.add(m);
                }
            }
            String jstr = listToJson(ls);
            lt = jsonToList(jstr, clazz);
        }
        return lt;
    }

    public static Boolean isExistField(String fieldName, Class clszz) {
        boolean flag = false;
        if (clszz == null) {
            return flag;
        }
        //获取这个类的所有属性
        Field[] fields = clszz.getDeclaredFields();
        if (fields == null || fields.length < 1) {
            return flag;
        }
        //循环遍历所有的fields
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    /**
     * 将流中的Excel数据转成List<Map>
     * 自定义最大条数
     *
     * @param request 输入流
     * @param request 文件名（判断Excel版本）
     * @param maps    形如new String[]{ "invoiceAmount&#需开票金额",
     *                "creator&#新建人",
     *                "createTime&#新建时间"   }
     *                字段名称映射
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static <T> List<T> parseExcel(HttpServletRequest request,
                                         String[] maps, Class<T> clazz, Integer maxRows) throws IOException {

        Map<String, String> mapping = new HashMap<>(16);
        for (String s : maps) {
            String[] sarry = s.split("&#");
            mapping.put(sarry[1], sarry[0]);
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Collection<List<MultipartFile>> value = multipartRequest.getMultiFileMap().values();

        List<MultipartFile> list = new ArrayList<MultipartFile>();
        for (List<MultipartFile> temp : value) {
            if (!temp.isEmpty()) {
                list.addAll(temp);
            }
        }
        MultipartFile file = list.get(0);
        InputStream in = file.getInputStream();
        // 根据文件名来创建Excel工作薄
        List<T> lt = new ArrayList<T>();
        Workbook work = getWorkbook(in, file.getOriginalFilename());
        if (null == work) {
            return lt;
        }

        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        // 返回数据
        List<Map<String, String>> ls = new ArrayList<Map<String, String>>();

        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            int rowss = sheet.getPhysicalNumberOfRows();
            if (rowss < 2) {
                return lt;

            }

            // 取第一行标题
            row = sheet.getRow(0);
            String[] title;
            if (row != null) {
                title = new String[row.getLastCellNum()];

                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    title[y] = getCellValue(cell);
                }

            } else {

                continue;
            }
            int rows = sheet.getPhysicalNumberOfRows();

            // 一次最多读取MaxRows行
            if (rows > maxRows + 1) {
                rows = maxRows + 1;
            }
            // 遍历当前sheet中的所有行
            int size = mapping.size();
            for (int j = 1; j < rows; j++) {
                row = sheet.getRow(j);
                Map<String, String> m = new HashMap<>(16);
                if (checkRowNull(row, size)) {
                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        // 判断是否为空行.空列
                        if (y < size) {
                            cell = row.getCell(y);
                            String key = title[y];
                            if (StringUtils.isEmpty(getCellValue(cell))) {
                                continue;
                            }
                            m.put(mapping.get(key), getCellValue(cell));
                        }
                    }
                    // 遍历所有的列
                    ls.add(m);
                }
            }
            String jstr = listToJson(ls);
            lt = jsonToList(jstr, clazz);
        }
        return lt;
    }

    /**
     * 判断行为空
     *
     * @param hssfRow
     * @param size
     * @return boolean
     */
    public static boolean checkRowNull(Row hssfRow, int size) {
        int num = 0;
        Iterator<Cell> cellItr = hssfRow.iterator();
        // 记录遍历次数
        int ci = 0;
        while (cellItr.hasNext()) {
            Cell c = cellItr.next();

            ci++;
            if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                num++;
            }
            if (ci == size && num == size) {
                //判断行为空
                return false;
            }
        }
        int blank = 0;
        cellItr = hssfRow.iterator();
        if (ci < size) {
            while (cellItr.hasNext()) {
                Cell c = cellItr.next();
                if (c.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                    blank++;
                }
                if (blank == ci) {
                    //判断行为空
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * List<T> 转 json 保存到数据库
     */
    public static <T> String listToJson(List<T> ts) {
        String jsons = JSON.toJSONString(ts);
        return jsons;
    }

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
}
