package zz.wohui.wohui365.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/18 15:12
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsListData {

    /**
     * State : 1
     * Data : {"List_CommRow":[{"SerialNumber":"e6035d32-f1c6-4d3e-9553-da4f47f7beb1","NumberSM":"af1794f0-ddc6-4ff3-b260-d84d12a757d6","NumberComm":"75727cab-52d7-437a-925d-059dee07ccd7","PriceUnit":0,"Integral":"0.00","CountCollected":0,"CountSaleTotal":"0","CountStockTotal":"0","NameComm":"好丽友 派 巧克力派 涂饰蛋类芯饼30枚/盒","BriefProduct":"好丽友，好朋友。","UrlListImage":""}]}
     * Msg :
     */

    private int State;
    private DataEntity Data;
    private String Msg;

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
        /**
         * SerialNumber : e6035d32-f1c6-4d3e-9553-da4f47f7beb1
         * NumberSM : af1794f0-ddc6-4ff3-b260-d84d12a757d6
         * NumberComm : 75727cab-52d7-437a-925d-059dee07ccd7
         * PriceUnit : 0
         * Integral : 0.00
         * CountCollected : 0
         * CountSaleTotal : 0
         * CountStockTotal : 0
         * NameComm : 好丽友 派 巧克力派 涂饰蛋类芯饼30枚/盒
         * BriefProduct : 好丽友，好朋友。
         * UrlListImage :
         */

        private List<ListCommRowEntity> List_CommRow;

        public void setList_CommRow(List<ListCommRowEntity> List_CommRow) {
            this.List_CommRow = List_CommRow;
        }

        public List<ListCommRowEntity> getList_CommRow() {
            return List_CommRow;
        }

        public static class ListCommRowEntity {
            private String SerialNumber;
            private String NumberSM;
            private String NumberComm;
            private int PriceUnit;
            private String Integral;
            private int CountCollected;
            private String CountSaleTotal;
            private String CountStockTotal;
            private String NameComm;
            private String BriefProduct;
            private String UrlListImage;

            public void setSerialNumber(String SerialNumber) {
                this.SerialNumber = SerialNumber;
            }

            public void setNumberSM(String NumberSM) {
                this.NumberSM = NumberSM;
            }

            public void setNumberComm(String NumberComm) {
                this.NumberComm = NumberComm;
            }

            public void setPriceUnit(int PriceUnit) {
                this.PriceUnit = PriceUnit;
            }

            public void setIntegral(String Integral) {
                this.Integral = Integral;
            }

            public void setCountCollected(int CountCollected) {
                this.CountCollected = CountCollected;
            }

            public void setCountSaleTotal(String CountSaleTotal) {
                this.CountSaleTotal = CountSaleTotal;
            }

            public void setCountStockTotal(String CountStockTotal) {
                this.CountStockTotal = CountStockTotal;
            }

            public void setNameComm(String NameComm) {
                this.NameComm = NameComm;
            }

            public void setBriefProduct(String BriefProduct) {
                this.BriefProduct = BriefProduct;
            }

            public void setUrlListImage(String UrlListImage) {
                this.UrlListImage = UrlListImage;
            }

            public String getSerialNumber() {
                return SerialNumber;
            }

            public String getNumberSM() {
                return NumberSM;
            }

            public String getNumberComm() {
                return NumberComm;
            }

            public int getPriceUnit() {
                return PriceUnit;
            }

            public String getIntegral() {
                return Integral;
            }

            public int getCountCollected() {
                return CountCollected;
            }

            public String getCountSaleTotal() {
                return CountSaleTotal;
            }

            public String getCountStockTotal() {
                return CountStockTotal;
            }

            public String getNameComm() {
                return NameComm;
            }

            public String getBriefProduct() {
                return BriefProduct;
            }

            public String getUrlListImage() {
                return UrlListImage;
            }
        }
    }
}