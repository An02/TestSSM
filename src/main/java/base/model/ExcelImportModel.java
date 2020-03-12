package base.model;

/**
 * description: excel导入中每条数据的处理结果
 *
 * @author tianhua.xie（tianhua.xie@ucarinc.com）
 * @date 2019-02-28 17:21:27
 * @version 1.0
 */
public class ExcelImportModel {

    private int index;
    private String key;
    private boolean result = true;
    private String msg;
    public ExcelImportModel() {
    }

    public ExcelImportModel(int index, String key, boolean result, String msg) {
        this.index = index;
        this.key = key;
        this.result = result;
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
