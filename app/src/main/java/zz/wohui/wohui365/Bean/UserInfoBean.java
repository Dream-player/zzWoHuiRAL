package zz.wohui.zz.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/3/9 13:53
 * 版本：V1.0
 * 修改历史：
 */
public class UserInfoBean {

    /**
     * State : 1
     * Data : {"respCode":"0000","respMsg":"成功","rowsCount":"1","data":[{"C_Number":"0000217320","C_RealName":"陈柯可","C_Phone":"95039090920","C_Province":"河北省","C_City":"邯郸市","C_District":"魏县","C_CanRegCount":"857.0000","C_Consume_Bonus":"97771.9140"}]}
     * Msg :
     */

    private int State;
    /**
     * respCode : 0000
     * respMsg : 成功
     * rowsCount : 1
     * data : [{"C_Number":"0000217320","C_RealName":"陈柯可","C_Phone":"95039090920","C_Province":"河北省","C_City":"邯郸市","C_District":"魏县","C_CanRegCount":"857.0000","C_Consume_Bonus":"97771.9140"}]
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
        private String respCode;
        private String respMsg;
        private String rowsCount;
        /**
         * C_Number : 0000217320
         * C_RealName : 陈柯可
         * C_Phone : 95039090920
         * C_Province : 河北省
         * C_City : 邯郸市
         * C_District : 魏县
         * C_CanRegCount : 857.0000
         * C_Consume_Bonus : 97771.9140
         */

        private List<dataEntity> data;

        public void setRespCode(String respCode) {
            this.respCode = respCode;
        }

        public void setRespMsg(String respMsg) {
            this.respMsg = respMsg;
        }

        public void setRowsCount(String rowsCount) {
            this.rowsCount = rowsCount;
        }

        public void setData(List<dataEntity> data) {
            this.data = data;
        }

        public String getRespCode() {
            return respCode;
        }

        public String getRespMsg() {
            return respMsg;
        }

        public String getRowsCount() {
            return rowsCount;
        }

        public List<dataEntity> getData() {
            return data;
        }

        public static class dataEntity {
            private String C_Number;
            private String C_RealName;
            private String C_Phone;
            private String C_Province;
            private String C_City;
            private String C_District;
            private String C_CanRegCount;
            private String C_Consume_Bonus;

            public void setC_Number(String C_Number) {
                this.C_Number = C_Number;
            }

            public void setC_RealName(String C_RealName) {
                this.C_RealName = C_RealName;
            }

            public void setC_Phone(String C_Phone) {
                this.C_Phone = C_Phone;
            }

            public void setC_Province(String C_Province) {
                this.C_Province = C_Province;
            }

            public void setC_City(String C_City) {
                this.C_City = C_City;
            }

            public void setC_District(String C_District) {
                this.C_District = C_District;
            }

            public void setC_CanRegCount(String C_CanRegCount) {
                this.C_CanRegCount = C_CanRegCount;
            }

            public void setC_Consume_Bonus(String C_Consume_Bonus) {
                this.C_Consume_Bonus = C_Consume_Bonus;
            }

            public String getC_Number() {
                return C_Number;
            }

            public String getC_RealName() {
                return C_RealName;
            }

            public String getC_Phone() {
                return C_Phone;
            }

            public String getC_Province() {
                return C_Province;
            }

            public String getC_City() {
                return C_City;
            }

            public String getC_District() {
                return C_District;
            }

            public String getC_CanRegCount() {
                return C_CanRegCount;
            }

            public String getC_Consume_Bonus() {
                return C_Consume_Bonus;
            }
        }
    }
}
