package zz.wohui.wohui365.Bean;

import java.io.Serializable;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/7 10:00
 * 版本：V1.0
 * 修改历史：
 */
public class ShopDetailBean implements Serializable{


    /**
     * State : 1
     * Data : {"SI_GUID":"260A4AA4085541488A58202EACF9BD5A","SI_SupermarketGUID":"C04550B1B1874417958775D639485A9F","SI_LinkMan_RealName":null,"SI_LinkMan_Phone":null,"SI_Province":"河南省","SI_City":"郑州市","SI_District":"高新区","SI_Towns":"翠竹街","SI_Address":"总部企业基地","SMN_Content":"本超市数据仅供测试，不能用于购买，请望谅解。","S_GUID":"C04550B1B1874417958775D639485A9F","S_Name":"我惠测试超市","S_HeadPhoto":"http://files.wohuiyun.com/yunchao.files/201610230947461667.jpg","S_SubDescribe":"平台测试超市，仅供查看不能购买。","S_RegTime":"2016-10-21T00:00:00","S_LinkMan_RealName":"我惠","S_LinkMan_Phone":"13803998721","S_QQ":"53479782","S_Long":113.572312,"S_Lat":34.824109,"S_Score":5,"S_CollectionNum":0,"S_IsBusiness":true,"S_Business_BeginHours":1,"S_Business_BeginMin":0,"S_Business_EndHours":13,"S_Business_EndMin":0,"S_BusinessRange":3000,"S_MinMoney":20,"S_Freight":5,"S_YWCard_GUID":"702876A0882E4E989CE1CFC867E64396","S_YWCard_Number":"13803998721","S_YCShop_GUID":"9BADBF236C2840D8BD27C700A64303C8","S_Status":10}
     * Msg :
     */

    private int State;
    private DataBean Data;
    private String Msg;

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public static class DataBean {
        /**
         * SI_GUID : 260A4AA4085541488A58202EACF9BD5A
         * SI_SupermarketGUID : C04550B1B1874417958775D639485A9F
         * SI_LinkMan_RealName : null
         * SI_LinkMan_Phone : null
         * SI_Province : 河南省
         * SI_City : 郑州市
         * SI_District : 高新区
         * SI_Towns : 翠竹街
         * SI_Address : 总部企业基地
         * SMN_Content : 本超市数据仅供测试，不能用于购买，请望谅解。
         * S_GUID : C04550B1B1874417958775D639485A9F
         * S_Name : 我惠测试超市
         * S_HeadPhoto : http://files.wohuiyun.com/yunchao.files/201610230947461667.jpg
         * S_SubDescribe : 平台测试超市，仅供查看不能购买。
         * S_RegTime : 2016-10-21T00:00:00
         * S_LinkMan_RealName : 我惠
         * S_LinkMan_Phone : 13803998721
         * S_QQ : 53479782
         * S_Long : 113.572312
         * S_Lat : 34.824109
         * S_Score : 5.0
         * S_CollectionNum : 0
         * S_IsBusiness : true
         * S_Business_BeginHours : 1
         * S_Business_BeginMin : 0
         * S_Business_EndHours : 13
         * S_Business_EndMin : 0
         * S_BusinessRange : 3000
         * S_MinMoney : 20.0
         * S_Freight : 5.0
         * S_YWCard_GUID : 702876A0882E4E989CE1CFC867E64396
         * S_YWCard_Number : 13803998721
         * S_YCShop_GUID : 9BADBF236C2840D8BD27C700A64303C8
         * S_Status : 10
         */

        private String SI_GUID;
        private String SI_SupermarketGUID;
        private Object SI_LinkMan_RealName;
        private Object SI_LinkMan_Phone;
        private String SI_Province;
        private String SI_City;
        private String SI_District;
        private String SI_Towns;
        private String SI_Address;
        private String SMN_Content;
        private String S_GUID;
        private String S_Name;
        private String S_HeadPhoto;
        private String S_SubDescribe;
        private String S_RegTime;
        private String S_LinkMan_RealName;
        private String S_LinkMan_Phone;
        private String S_QQ;
        private double S_Long;
        private double S_Lat;
        private float S_Score;
        private int S_CollectionNum;
        private boolean S_IsBusiness;
        private int S_Business_BeginHours;
        private int S_Business_BeginMin;
        private int S_Business_EndHours;
        private int S_Business_EndMin;
        private int S_BusinessRange;
        private double S_MinMoney;
        private double S_Freight;
        private String S_YWCard_GUID;
        private String S_YWCard_Number;
        private String S_YCShop_GUID;
        private int S_Status;

        public String getSI_GUID() {
            return SI_GUID;
        }

        public void setSI_GUID(String SI_GUID) {
            this.SI_GUID = SI_GUID;
        }

        public String getSI_SupermarketGUID() {
            return SI_SupermarketGUID;
        }

        public void setSI_SupermarketGUID(String SI_SupermarketGUID) {
            this.SI_SupermarketGUID = SI_SupermarketGUID;
        }

        public Object getSI_LinkMan_RealName() {
            return SI_LinkMan_RealName;
        }

        public void setSI_LinkMan_RealName(Object SI_LinkMan_RealName) {
            this.SI_LinkMan_RealName = SI_LinkMan_RealName;
        }

        public Object getSI_LinkMan_Phone() {
            return SI_LinkMan_Phone;
        }

        public void setSI_LinkMan_Phone(Object SI_LinkMan_Phone) {
            this.SI_LinkMan_Phone = SI_LinkMan_Phone;
        }

        public String getSI_Province() {
            return SI_Province;
        }

        public void setSI_Province(String SI_Province) {
            this.SI_Province = SI_Province;
        }

        public String getSI_City() {
            return SI_City;
        }

        public void setSI_City(String SI_City) {
            this.SI_City = SI_City;
        }

        public String getSI_District() {
            return SI_District;
        }

        public void setSI_District(String SI_District) {
            this.SI_District = SI_District;
        }

        public String getSI_Towns() {
            return SI_Towns;
        }

        public void setSI_Towns(String SI_Towns) {
            this.SI_Towns = SI_Towns;
        }

        public String getSI_Address() {
            return SI_Address;
        }

        public void setSI_Address(String SI_Address) {
            this.SI_Address = SI_Address;
        }

        public String getSMN_Content() {
            return SMN_Content;
        }

        public void setSMN_Content(String SMN_Content) {
            this.SMN_Content = SMN_Content;
        }

        public String getS_GUID() {
            return S_GUID;
        }

        public void setS_GUID(String S_GUID) {
            this.S_GUID = S_GUID;
        }

        public String getS_Name() {
            return S_Name;
        }

        public void setS_Name(String S_Name) {
            this.S_Name = S_Name;
        }

        public String getS_HeadPhoto() {
            return S_HeadPhoto;
        }

        public void setS_HeadPhoto(String S_HeadPhoto) {
            this.S_HeadPhoto = S_HeadPhoto;
        }

        public String getS_SubDescribe() {
            return S_SubDescribe;
        }

        public void setS_SubDescribe(String S_SubDescribe) {
            this.S_SubDescribe = S_SubDescribe;
        }

        public String getS_RegTime() {
            return S_RegTime;
        }

        public void setS_RegTime(String S_RegTime) {
            this.S_RegTime = S_RegTime;
        }

        public String getS_LinkMan_RealName() {
            return S_LinkMan_RealName;
        }

        public void setS_LinkMan_RealName(String S_LinkMan_RealName) {
            this.S_LinkMan_RealName = S_LinkMan_RealName;
        }

        public String getS_LinkMan_Phone() {
            return S_LinkMan_Phone;
        }

        public void setS_LinkMan_Phone(String S_LinkMan_Phone) {
            this.S_LinkMan_Phone = S_LinkMan_Phone;
        }

        public String getS_QQ() {
            return S_QQ;
        }

        public void setS_QQ(String S_QQ) {
            this.S_QQ = S_QQ;
        }

        public double getS_Long() {
            return S_Long;
        }

        public void setS_Long(double S_Long) {
            this.S_Long = S_Long;
        }

        public double getS_Lat() {
            return S_Lat;
        }

        public void setS_Lat(double S_Lat) {
            this.S_Lat = S_Lat;
        }

        public float getS_Score() {
            return S_Score;
        }

        public void setS_Score(float S_Score) {
            this.S_Score = S_Score;
        }

        public int getS_CollectionNum() {
            return S_CollectionNum;
        }

        public void setS_CollectionNum(int S_CollectionNum) {
            this.S_CollectionNum = S_CollectionNum;
        }

        public boolean isS_IsBusiness() {
            return S_IsBusiness;
        }

        public void setS_IsBusiness(boolean S_IsBusiness) {
            this.S_IsBusiness = S_IsBusiness;
        }

        public int getS_Business_BeginHours() {
            return S_Business_BeginHours;
        }

        public void setS_Business_BeginHours(int S_Business_BeginHours) {
            this.S_Business_BeginHours = S_Business_BeginHours;
        }

        public int getS_Business_BeginMin() {
            return S_Business_BeginMin;
        }

        public void setS_Business_BeginMin(int S_Business_BeginMin) {
            this.S_Business_BeginMin = S_Business_BeginMin;
        }

        public int getS_Business_EndHours() {
            return S_Business_EndHours;
        }

        public void setS_Business_EndHours(int S_Business_EndHours) {
            this.S_Business_EndHours = S_Business_EndHours;
        }

        public int getS_Business_EndMin() {
            return S_Business_EndMin;
        }

        public void setS_Business_EndMin(int S_Business_EndMin) {
            this.S_Business_EndMin = S_Business_EndMin;
        }

        public int getS_BusinessRange() {
            return S_BusinessRange;
        }

        public void setS_BusinessRange(int S_BusinessRange) {
            this.S_BusinessRange = S_BusinessRange;
        }

        public double getS_MinMoney() {
            return S_MinMoney;
        }

        public void setS_MinMoney(double S_MinMoney) {
            this.S_MinMoney = S_MinMoney;
        }

        public double getS_Freight() {
            return S_Freight;
        }

        public void setS_Freight(double S_Freight) {
            this.S_Freight = S_Freight;
        }

        public String getS_YWCard_GUID() {
            return S_YWCard_GUID;
        }

        public void setS_YWCard_GUID(String S_YWCard_GUID) {
            this.S_YWCard_GUID = S_YWCard_GUID;
        }

        public String getS_YWCard_Number() {
            return S_YWCard_Number;
        }

        public void setS_YWCard_Number(String S_YWCard_Number) {
            this.S_YWCard_Number = S_YWCard_Number;
        }

        public String getS_YCShop_GUID() {
            return S_YCShop_GUID;
        }

        public void setS_YCShop_GUID(String S_YCShop_GUID) {
            this.S_YCShop_GUID = S_YCShop_GUID;
        }

        public int getS_Status() {
            return S_Status;
        }

        public void setS_Status(int S_Status) {
            this.S_Status = S_Status;
        }
    }
}
