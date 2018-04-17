package zz.wohui.wohui365.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/14 9:37
 * 版本：V1.0
 * 修改历史：
 */
public class WXPrepayIDBean {


    /**
     * State : 1
     * Data : {"appid":"wx13bf97f80b2a6343","partnerid":"1346795901","prepayid":"wx2016102009391044bcdbeeff0765294940","noncestr":"135eb5a5c1374a18a77c0f011b2ab46d","timestamp":"20161020103910","package":"Sign=WXPay","sign":"B9761931F1FB10788365103EDFCC47D4"}
     * Msg :
     */

    private int State;
    /**
     * appid : wx13bf97f80b2a6343
     * partnerid : 1346795901
     * prepayid : wx2016102009391044bcdbeeff0765294940
     * noncestr : 135eb5a5c1374a18a77c0f011b2ab46d
     * timestamp : 20161020103910
     * package : Sign=WXPay
     * sign : B9761931F1FB10788365103EDFCC47D4
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
        private String appid;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getAppid() {
            return appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getPackageX() {
            return packageX;
        }

        public String getSign() {
            return sign;
        }
    }
}
