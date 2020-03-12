package com.manage.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel工具类
 *
 * @author system
 * @version 1.0
 * @date 2019/5/16 9:24
 * @copyright www.ucarinc.com All Rights Reserved.
 */
public class Excel2007Util {
    private static String ZERO = "0";
    private static String ZERO_ONE = "0.0";
    private static String ZERO_TWO = "0.00";

    private Workbook wb = null;
    private Sheet sheet = null;
    private Row row = null;
    private Cell cell = null;
    private CellStyle headStyle = null;
    private CellStyle contentStyle = null;
    private static Logger LOGGER = LoggerFactory.getLogger(Excel2007Util.class);
    private static DataFormat format = null;

    public static DataFormat getFormat(Workbook wb) {
        if (format == null) {
            format = wb.createDataFormat();
        }
        return format;
    }

    /**
     * 保存工作表到磁盘
     *
     * @param @param dir
     * @Title: makeDir
     */
    public static boolean save(OutputStream os, XSSFWorkbook book) {
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
    private static XSSFRow getRow(XSSFSheet sht, int index) {
        XSSFRow row = sht.getRow(index);
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
    private static XSSFCell getCell(XSSFRow row, int index) {
        XSSFCell cell = row.getCell(index);
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
        XSSFWorkbook wb = new XSSFWorkbook();
        CellStyle headStyle = headStyle(wb);
        XSSFSheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(20);
        wb.setSheetName(0, "数据列表");
        XSSFRow row = sheet.createRow(0);
        row.setHeight((short) 450);
        XSSFCell cell;// 单元格

        List<String> headKeys = new ArrayList<String>();

        String[] strarrxx;
        for (int i = 0; i < selectColumns.length; i++) {
            strarrxx = selectColumns[i].split("&#");
            headKeys.add(strarrxx[0]);
            cell = row.createCell(i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
            cell.setCellStyle(headStyle);
        }

        CellStyle contentStyle = contentStyle(wb);

        // 写内容
        for (int k = 0; k < list.size(); k++) {
            // 获取到map之后，利用headkey得到此map的值，写入excel
            Map<String, String> rowmap = list.get(k);
            // 从第二行开始写
            row = sheet.createRow(k + 1);
            row.setHeight((short) 300);
            for (int i = 0; i < headKeys.size(); i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
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

    public static void exportForListPages(String[] selectColumns, List<Map<String, String>> list, OutputStream outputStream) throws IOException {

        Workbook wb = new SXSSFWorkbook(100);
        CellStyle headStyle = headStyle(wb);
        Sheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(20);
        wb.setSheetName(0, "数据列表");
        Row row = sheet.createRow(0);
        row.setHeight((short) 450);
        Cell cell;// 单元格

        List<String> headKeys = new ArrayList<String>();

        String[] strarrxx;
        for (int i = 0; i < selectColumns.length; i++) {
            strarrxx = selectColumns[i].split("&#");
            headKeys.add(strarrxx[0]);
            cell = row.createCell(i);
            cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
            cell.setCellStyle(headStyle);
        }

        CellStyle contentStyle = contentStyle(wb);
        LOGGER.info("XSSFWorkbook写内容");
        // 写内容
        for (int k = 0; k < list.size(); k++) {
            // 获取到map之后，利用headkey得到此map的值，写入excel
            Map<String, String> rowmap = list.get(k);
            // 从第二行开始写
            row = sheet.createRow(k + 1);
            row.setHeight((short) 300);
            for (int i = 0; i < headKeys.size(); i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String value = rowmap.get(headKeys.get(i)) == null ? "" : rowmap.get(headKeys.get(i));
                if (value.contains(".") && isDouble(value) && value.length() < 10) {
                    cell.setCellValue(Double.valueOf(value));
                } else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(contentStyle);
            }

        }
        LOGGER.info("XSSFWorkbook写内容完成");
        // 将流读到内存里面，在内存里构造好一个输入输出流，直接传到浏览器端,不会生成临时文件

        wb.write(outputStream);
        LOGGER.info("XSSFWorkbook写文件完成");


    }

    /**
     * 设置导出到的excel的样式
     *
     * @param wb
     * @return
     */
    private static CellStyle contentStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(f);
        // 上下居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setDataFormat(getFormat(wb).getFormat("@"));
        return style;
    }

    /**
     * 设置导出到的excel的样式-字体颜色为红色
     *
     * @param wb
     * @return
     */
    private XSSFCellStyle contentStyle4Red(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.RED.index);
        f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
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
    private static CellStyle headStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
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
    private XSSFCellStyle leftAlignStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        // 字号
        f.setFontHeightInPoints((short) 11);
        f.setColor(HSSFColor.BLACK.index);
        f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
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
        Excel2007Util excelUtil = new Excel2007Util();
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

    private void writeAccountDataToExcel(XSSFWorkbook wb, String[] selectColumns, XSSFCellStyle headStyle,
                                         List<Map<String, String>> list, Integer existFlag, String sheetName) {
        XSSFSheet sheet = wb.createSheet();
        sheet.setDefaultColumnWidth(20);
        wb.setSheetName(existFlag, sheetName);
        XSSFRow row = sheet.createRow(0);
        row.setHeight((short) 450);
        XSSFCell cell;// 单元格

        List<String> headKeys = new ArrayList<String>();

        String[] strarrxx;
        for (int i = 0; i < selectColumns.length; i++) {
            strarrxx = selectColumns[i].split("&#");
            headKeys.add(strarrxx[0]);
            cell = row.createCell(i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(strarrxx.length > 1 ? strarrxx[1] : "");
            cell.setCellStyle(headStyle);
        }

        CellStyle contentStyle = contentStyle(wb);

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

    public void init() {
        this.wb = new SXSSFWorkbook(100);
        this.headStyle = headStyle(wb);
        this.contentStyle = contentStyle(wb);
    }

    public void writeData(List<List<String>> listDate, int sheetIndex) {
        List<String> rowList;
        for (int k = 0; k < listDate.size(); k++) {
            rowList = listDate.get(k);
            row = sheet.createRow(sheetIndex++);
            row.setHeight((short) 300);
            for (int i = 0; i < rowList.size(); i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String value = rowList.get(i) == null ? "" : rowList.get(i);
                if (value.contains(".") && isDouble(value) && value.length() < 10) {
                    cell.setCellValue(Double.valueOf(value));
                } else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(contentStyle);
            }

        }
    }

    public Sheet handleSheet(String name, String[] titles) {
        sheet = wb.createSheet(name);
        row = sheet.createRow(0);
        row.setHeight((short) 450);
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(headStyle);
        }
        return sheet;
    }

    public void save(OutputStream outputStream) throws IOException {
        wb.write(outputStream);
    }

    /**
     * 自适应宽度(中文支持)
     *
     * @param sheet
     * @return void
     */
    public void setColumnWidth(Sheet sheet, int size) {
        // 获取当前列的宽度，然后对比本列的长度，取最大值
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes(Charset.forName("UTF-8")).length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            // 列宽最大支持255
            if (columnWidth < 256) {
                sheet.setColumnWidth(columnNum, columnWidth * 256);
            }
        }
    }

}
