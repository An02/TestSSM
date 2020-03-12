package base.model;

import java.util.List;

/**
 * description: excel导入的处理结果集
 *
 * @author tianhua.xie（tianhua.xie@ucarinc.com）
 * @date 2019-02-28 17:21:42
 * @version 1.0
 */
public class ExcelImportResult {

    /** 总条数 */
    private int sumNum;
    /** 成功条数 */
    private int sucNum = 0;
    /** 失败条数 */
    private int errNum = 0;
    /** 每条处理结果 */
    private List<ExcelImportModel> items;
    /** 整体返回消息 */
    private String msg;
    /** 详情信息 */
    private String detailMsg;

    public ExcelImportResult() {
    }

    public ExcelImportResult(int sucNum, int errNum, List<ExcelImportModel> items) {
        this.sucNum = sucNum;
        this.errNum = errNum;
        this.items = items;
    }

    public ExcelImportResult(String msg) {
        this.msg = msg;
    }

    public int getSumNum() {
        return sumNum;
    }

    public void setSumNum(int sumNum) {
        this.sumNum = sumNum;
    }

    public int getSucNum() {
        return sucNum;
    }

    public void setSucNum(int sucNum) {
        this.sucNum = sucNum;
    }

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public List<ExcelImportModel> getItems() {
        return items;
    }

    public void setItems(List<ExcelImportModel> items) {
        this.items = items;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }
}
