package whcs.wohui.zz.Bean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/7 11:39
 * 版本：V1.0
 * 修改历史：
 */
public class IsSuccessBean {

    /**
     * IsSuccess : true
     * Message : 添加成功!
     * Data : null
     * DataCount : null
     */

    private boolean IsSuccess;
    private String Message;
    private Object Data;
    private Object DataCount;

    public void setIsSuccess(boolean IsSuccess) {
        this.IsSuccess = IsSuccess;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }

    public void setDataCount(Object DataCount) {
        this.DataCount = DataCount;
    }

    public boolean isIsSuccess() {
        return IsSuccess;
    }

    public String getMessage() {
        return Message;
    }

    public Object getData() {
        return Data;
    }

    public Object getDataCount() {
        return DataCount;
    }
}
