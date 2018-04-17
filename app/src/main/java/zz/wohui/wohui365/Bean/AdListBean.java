package zz.wohui.zz.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/29 17:34
 * 版本：V1.0
 * 修改历史：
 */
public class AdListBean {

    /**
     * State : 1
     * Data : [{"AD_GUID":"33B99A01E1E0498D94BAA90D8B9B323D","AD_SMGUID":"","AD_Title":"购物流程","AD_ImgUrl":"http://files.wohuiyun.com/yunchao.files/201609291115391317.jpg","AD_LinkUrl":"http://www.wohui365.com","AD_SeatCode":"001","AD_Status":true,"AD_Sort":1}]
     * Msg :
     */

    private int State;
    private String Msg;
    /**
     * AD_GUID : 33B99A01E1E0498D94BAA90D8B9B323D
     * AD_SMGUID :
     * AD_Title : 购物流程
     * AD_ImgUrl : http://files.wohuiyun.com/yunchao.files/201609291115391317.jpg
     * AD_LinkUrl : http://www.wohui365.com
     * AD_SeatCode : 001
     * AD_Status : true
     * AD_Sort : 1
     */

    private List<DataEntity> Data;

    public void setState(int State) {
        this.State = State;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public int getState() {
        return State;
    }

    public String getMsg() {
        return Msg;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public static class DataEntity {
        private String AD_GUID;
        private String AD_SMGUID;
        private String AD_Title;
        private String AD_ImgUrl;
        private String AD_LinkUrl;
        private String AD_SeatCode;
        private boolean AD_Status;
        private int AD_Sort;

        public void setAD_GUID(String AD_GUID) {
            this.AD_GUID = AD_GUID;
        }

        public void setAD_SMGUID(String AD_SMGUID) {
            this.AD_SMGUID = AD_SMGUID;
        }

        public void setAD_Title(String AD_Title) {
            this.AD_Title = AD_Title;
        }

        public void setAD_ImgUrl(String AD_ImgUrl) {
            this.AD_ImgUrl = AD_ImgUrl;
        }

        public void setAD_LinkUrl(String AD_LinkUrl) {
            this.AD_LinkUrl = AD_LinkUrl;
        }

        public void setAD_SeatCode(String AD_SeatCode) {
            this.AD_SeatCode = AD_SeatCode;
        }

        public void setAD_Status(boolean AD_Status) {
            this.AD_Status = AD_Status;
        }

        public void setAD_Sort(int AD_Sort) {
            this.AD_Sort = AD_Sort;
        }

        public String getAD_GUID() {
            return AD_GUID;
        }

        public String getAD_SMGUID() {
            return AD_SMGUID;
        }

        public String getAD_Title() {
            return AD_Title;
        }

        public String getAD_ImgUrl() {
            return AD_ImgUrl;
        }

        public String getAD_LinkUrl() {
            return AD_LinkUrl;
        }

        public String getAD_SeatCode() {
            return AD_SeatCode;
        }

        public boolean isAD_Status() {
            return AD_Status;
        }

        public int getAD_Sort() {
            return AD_Sort;
        }
    }
}
