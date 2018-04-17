package zz.wohui.wohui365.Bean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/18 17:35
 * 版本：V1.0
 * 修改历史：
 */
public class AddCartBean {

    /**
     * State : 1
     * Data : {"IsSuccess":true,"Message":"添加成功!","Data":null,"DataCount":null}
     * Msg :
     */

    private int State;
    /**
     * IsSuccess : true
     * Message : 添加成功!
     * Data : null
     * DataCount : null
     */

    private DataEntity Data;
    private String Msg;

    public void setState(int State) {
        this.State = State;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getState() {
        return State;
    }

    public DataEntity getData() {
        return Data;
    }

    public String getMsg() {
        return Msg;
    }

    public static class DataEntity {
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
}
