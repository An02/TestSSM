package com.manage.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author baichuan.wu
 * @version 1.0.0
 * @date 2020-02-08 13:34
 */
public class ExcelUtil {
    private static String ZERO = "0";
    private static String ZERO_ONE = "0.0";
    private static String ZERO_TWO = "0.00";

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private String sheetName;


    /**
     * 保存工作表到磁盘
     *
     * @param os
     * @param book
     * @return boolean
     */
    public static boolean save(OutputStream os, HSSFWorkbook book) {
        try {
            book.write(os);
            os.flush();
            os.close();
            return true;
        } catch (Exception e) {
            LOGGER.error("保存excel文件异常", e);
            return false;
        }
    }

    /**
     * 获取工作表中指定的行
     *
     * @param sht   工作表
     * @param index 工作行索引
     * @return
     */
    private static HSSFRow getRow(HSSFSheet sht, int index) {
        HSSFRow row = sht.getRow(index);
        if (null == row) {
            row = sht.createRow(index);
        }
        return row;
    }

    /**
     * 获取工作表中指定的列
     *
     * @param row   工作行
     * @param index 工作列索引
     * @return
     */
    private static HSSFCell getCell(HSSFRow row, int index) {
        HSSFCell cell = row.getCell(index);
        if (null == cell) {
            cell = row.createCell(index, Cell.CELL_TYPE_STRING);
        }
        return cell;
    }

    /**
     * 根据属性字段获取数据
     *
     * @param bean 数据实体
     * @param prop 属性
     * @return
     */
    private static Object getBeanVal(Object bean, String prop) {
        Object itemValue = null;
        try {
            itemValue = PropertyUtils.getProperty(bean, prop);
        } catch (Exception e) {
            LOGGER.debug("获取数据出现异常，beanName=" + bean.getClass().getName() + "\tprop=" + prop, e);
        }
        return itemValue;
    }

    /**
     * 导出数据
     */
    public InputStream exportForListPage(String[] selectColumns, List<Map<String, String>> list) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle headStyle = headStyle(wb);
        HSSFSheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(20);
        wb.setSheetName(0, sheetName == null ? "数据列表" : sheetName);
        // 行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 450);
        HSSFCell cell;// 单元格

        List<String> headKeys = new ArrayList<String>();

        String[] strarrxx;
        for (int i = 0; i < selectColumns.length; i++) {
            strarrxx = selectColumns[i].split("&#");
            headKeys.add(strarrxx[0]);
            cell = row.createCell(i);
            cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
            cell.setCellStyle(headStyle);
        }

        HSSFCellStyle contentStyle = contentStyle(wb);

        // 写内容
        for (int k = 0; k < list.size(); k++) {
            // 获取到map之后，利用headkey得到此map的值，写入excel
            Map<String, String> rowmap = list.get(k);
            // 从第二行开始写
            row = sheet.createRow(k + 1);
            row.setHeight((short) 300);
            for (int i = 0; i < headKeys.size(); i++) {
                cell = row.createCell(i);
                String value = rowmap.get(headKeys.get(i)) == null ? "" : rowmap.get(headKeys.get(i));
                if (value.contains(".") && isDouble(value) && value.length() < 10) {
                    cell.setCellValue(Double.valueOf(value));
                } else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(contentStyle);
            }
        }
        // 将流读到内存里面，在内存里构造好一个输入输出流，直接传到浏览器端,不会生成临时文件
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = null;
        is = new ByteArrayInputStream(content);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public void exportForListPages(String[] selectColumns, List<Map<String, String>> list, OutputStream outputStream) throws IOException {
        int k = 0;
        int max = 65535;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle headStyle = headStyle(wb);
        int e = list.size() % max == 0 ? list.size() / max : list.size() / max + 1;
        LOGGER.error("HSSFWorkbook开始写数据");
        for (int s = 0; s < e; s++) {
            LOGGER.error("HSSFWorkbook第" + s + "sheet页");
            HSSFSheet sheet = wb.createSheet();
            sheet.setDefaultColumnWidth(20);
            wb.setSheetName(s, sheetName == null ? "数据列表" + s : sheetName);
            // 行
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short) 450);
            HSSFCell cell;// 单元格

            List<String> headKeys = new ArrayList<String>();

            String[] strarrxx;
            for (int i = 0; i < selectColumns.length; i++) {
                strarrxx = selectColumns[i].split("&#");
                headKeys.add(strarrxx[0]);
                cell = row.createCell(i);
                cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
                cell.setCellStyle(headStyle);
            }

            HSSFCellStyle contentStyle = contentStyle(wb);

            // 写内容
            int maxLen = 65535;
            for (int r = 1; r <= maxLen; r++) {
                if (k > list.size() - 1) {
                    break;
                }
                // 获取到map之后，利用headkey得到此map的值，写入excel
                Map<String, String> rowmap = list.get(k);
                // 从第二行开始写
                row = sheet.createRow(r);
                row.setHeight((short) 300);
                for (int i = 0; i < headKeys.size(); i++) {
                    cell = row.createCell(i);
                    String value = rowmap.get(headKeys.get(i)) == null ? "" : rowmap.get(headKeys.get(i));
                    if (value.contains(".") && isDouble(value) && value.length() < 10) {
                        cell.setCellValue(Double.valueOf(value));
                    } else {
                        cell.setCellValue(value);
                    }
                    cell.setCellStyle(contentStyle);
                }
                k++;
            }
        }

        // 将流读到内存里面，在内存里构造好一个输入输出流，直接传到浏览器端,不会生成临时文件
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        LOGGER.error("HSSFWorkbook写数据完成开始写文件");
        //wb.write(os);outputStream
        //打印wb的大小
        wb.write(outputStream);
        LOGGER.error("HSSFWorkbook写文件完成");
        //byte[] content = os.toByteArray();
        //InputStream is = null;
        //is = new ByteArrayInputStream(content);
        //os.close();
        //return is;
    }

    /**
     * 设置导出到的excel的样式
     *
     * @param wb
     * @return
     */
    private HSSFCellStyle contentStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(f);
        // 上下居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        return style;
    }

    /**
     * 设置导出到的excel的样式-字体颜色为红色
     *
     * @param wb
     * @return
     */
    private HSSFCellStyle contentStyle4Red(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.RED.index);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(f);
        // 上下居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        return style;
    }

    /**
     * 设置导出到的excel的样式
     *
     * @param wb
     * @return
     */
    private HSSFCellStyle headStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(f);
        // 上下居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        return style;
    }

    /**
     * 左边对齐
     *
     * @param wb
     * @return
     */
    private HSSFCellStyle leftAlignStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(f);
        // 上下居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        return style;
    }

    public static void createExcelContentType(String excelName,
                                              HttpServletResponse response) throws UnsupportedEncodingException {
        String showFileName = URLEncoder.encode(excelName + ".xls", "UTF-8");
        showFileName = new String(showFileName.getBytes("iso8859-1"), "gb2312");
        // 定义输出类型
        response.setContentType("application/msexcel");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(showFileName.getBytes("gb2312"), "iso8859-1"));
    }

    /**
     * @param @param  str
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isDouble
     * @Description: 判断字符串是否为浮点数
     */
    public static boolean isDouble(String str) {
        Boolean result = str.matches("-?[0-9]?.+[0-9]*");
        if (!result) {
            if (StringUtils.equals(ZERO, str) || StringUtils.equals(ZERO_ONE, str) || StringUtils.equals(ZERO_TWO, str)) {
                result = true;
            }
        }
        if (result) {
            try {
                Double.valueOf(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    /**
     * @param @param  str
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: isDouble
     * @Description: 判断字符串是否为整数
     */
    public static boolean isLong(String str) {
        Boolean result = str.matches("[0-9]+");
        if (result) {
            try {
                Long.valueOf(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 构建Excel文件byte数组
     *
     * @param excelTitle excel表头
     * @param list       excel行数据
     * @return byte[]
     */
    public static byte[] buildExcelByteData(String[] excelTitle, List<Map<String, String>> list) {
        ExcelUtil excelUtil = new ExcelUtil();
        InputStream inputStream = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            inputStream = excelUtil.exportForListPage(excelTitle, list);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, len);
            }
            bytes = null;
            return outStream.toByteArray();
        } catch (IOException ioE) {
            LOGGER.error("build excel byte data error: ", ioE);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("build excel byte data error: ", e);
            }
        }
        return null;
    }

    public InputStream exportForListPage(String[] accountSheetColumnArr, String[] accountSheetItemColumnArr, List<Map<String, String>> selflist,
                                         List<Map<String, String>> mmclist, List<Map<String, String>> selfItemlist,
                                         List<Map<String, String>> mmcItemlist) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle headStyle = headStyle(wb);
        writeAccountDataToExcel(wb, accountSheetColumnArr, headStyle, selflist, 0, "自建厂对账单");
        writeAccountDataToExcel(wb, accountSheetColumnArr, headStyle, mmclist, 1, "买买车对账单");
        writeAccountDataToExcel(wb, accountSheetItemColumnArr, headStyle, selfItemlist, 2, "自建厂对账单明细");
        writeAccountDataToExcel(wb, accountSheetItemColumnArr, headStyle, mmcItemlist, 3, "买买车对账单明细");
        // 将流读到内存里面，在内存里构造好一个输入输出流，直接传到浏览器端,不会生成临时文件
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = os.toByteArray();
        InputStream is = null;
        is = new ByteArrayInputStream(content);
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    private void writeAccountDataToExcel(HSSFWorkbook wb, String[] selectColumns, HSSFCellStyle headStyle,
                                         List<Map<String, String>> list, Integer existFlag, String sheetName) {
        HSSFSheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(20);
        wb.setSheetName(existFlag, sheetName);
        // 行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 450);
        HSSFCell cell;// 单元格

        List<String> headKeys = new ArrayList<String>();

        String[] strarrxx;
        for (int i = 0; i < selectColumns.length; i++) {
            strarrxx = selectColumns[i].split("&#");
            headKeys.add(strarrxx[0]);
            cell = row.createCell(i);
            cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
            cell.setCellStyle(headStyle);
        }

        HSSFCellStyle contentStyle = contentStyle(wb);

        // 写内容
        for (int k = 0; k < list.size(); k++) {
            // 获取到map之后，利用headkey得到此map的值，写入excel
            Map<String, String> rowmap = list.get(k);
            // 从第二行开始写
            row = sheet.createRow(k + 1);
            row.setHeight((short) 300);
            for (int i = 0; i < headKeys.size(); i++) {
                cell = row.createCell(i);
                String value = rowmap.get(headKeys.get(i)) == null ? "" : rowmap.get(headKeys.get(i));
                if (i != 19 && i != 21 && isDouble(value) && value.length() < 20) {
                    cell.setCellValue(Double.valueOf(value));
                } else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(contentStyle);
            }
        }

    }


}
