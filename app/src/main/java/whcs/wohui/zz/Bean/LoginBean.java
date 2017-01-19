package whcs.wohui.zz.Bean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/7 10:21
 * 版本：V1.0
 * 修改历史：
 */
public class LoginBean {

    /**
     * State : 1
     * Data : {"UMsg":"success","UID":"336225","UGUID":"702876A0882E4E989CE1CFC867E64396","UNumber":"6622052801232998"}
     * Msg : 登陆成功
     */

    private int State;
    /**
     * UMsg : success
     * UID : 336225
     * UGUID : 702876A0882E4E989CE1CFC867E64396
     * UNumber : 6622052801232998
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
        private String UMsg;
        private String UID;
        private String UGUID;
        private String UNumber;

        public void setUMsg(String UMsg) {
            this.UMsg = UMsg;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public void setUGUID(String UGUID) {
            this.UGUID = UGUID;
        }

        public void setUNumber(String UNumber) {
            this.UNumber = UNumber;
        }

        public String getUMsg() {
            return UMsg;
        }

        public String getUID() {
            return UID;
        }

        public String getUGUID() {
            return UGUID;
        }

        public String getUNumber() {
            return UNumber;
        }
    }
}
