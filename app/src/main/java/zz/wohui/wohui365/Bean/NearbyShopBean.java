package zz.wohui.wohui365.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/12 15:55
 * 版本：V1.0
 * 修改历史：
 */
public class NearbyShopBean implements Serializable{


    /**
     * State : 1
     * Data : [{"S_GUID":"4E1AFD6B196341E68C49C1B3AB07E30F","S_Name":"我惠期间超市","S_HeadPhoto":"http://files.wohuiyun.com/m.yw.files/201608170955382145.png","S_Long":113.572337,"S_Lat":34.824047,"S_Score":2.2,"S_CollectionNum":0,"S_IsBusiness":true,"S_Business_BeginHours":9,"S_Business_BeginMin":30,"S_Business_EndHours":18,"S_Business_EndMin":30,"S_BusinessRange":100,"S_MinMoney":4,"S_Freight":5,"S_YWCard_GUID":"702876A0882E4E989CE1CFC867E64396","S_YCShop_GUID":" ","S_Status":10}]
     * Msg :
     */

    private int State;
    private String Msg;
    /**
     * S_GUID : 4E1AFD6B196341E68C49C1B3AB07E30F
     * S_Name : 我惠期间超市
     * S_HeadPhoto : http://files.wohuiyun.com/m.yw.files/201608170955382145.png
     * S_Long : 113.572337
     * S_Lat : 34.824047
     * S_Score : 2.2
     * S_CollectionNum : 0
     * S_IsBusiness : true
     * S_Business_BeginHours : 9
     * S_Business_BeginMin : 30
     * S_Business_EndHours : 18
     * S_Business_EndMin : 30
     * S_BusinessRange : 100
     * S_MinMoney : 4
     * S_Freight : 5
     * S_YWCard_GUID : 702876A0882E4E989CE1CFC867E64396
     * S_YCShop_GUID :
     * S_Status : 10
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
        private double Distance;



        private String S_GUID;
        private String S_Name;
        private String S_HeadPhoto;
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
        private int S_Freight;
        private String S_YWCard_GUID;
        private String S_YCShop_GUID;
        private int S_Status;
        public double getDistance() {
            return Distance;
        }

        public void setDistance(double distance) {
            Distance = distance;
        }
        public void setS_GUID(String S_GUID) {
            this.S_GUID = S_GUID;
        }

        public void setS_Name(String S_Name) {
            this.S_Name = S_Name;
        }

        public void setS_HeadPhoto(String S_HeadPhoto) {
            this.S_HeadPhoto = S_HeadPhoto;
        }

        public void setS_Long(double S_Long) {
            this.S_Long = S_Long;
        }

        public void setS_Lat(double S_Lat) {
            this.S_Lat = S_Lat;
        }

        public void setS_Score(float S_Score) {
            this.S_Score = S_Score;
        }

        public void setS_CollectionNum(int S_CollectionNum) {
            this.S_CollectionNum = S_CollectionNum;
        }

        public void setS_IsBusiness(boolean S_IsBusiness) {
            this.S_IsBusiness = S_IsBusiness;
        }

        public void setS_Business_BeginHours(int S_Business_BeginHours) {
            this.S_Business_BeginHours = S_Business_BeginHours;
        }

        public void setS_Business_BeginMin(int S_Business_BeginMin) {
            this.S_Business_BeginMin = S_Business_BeginMin;
        }

        public void setS_Business_EndHours(int S_Business_EndHours) {
            this.S_Business_EndHours = S_Business_EndHours;
        }

        public void setS_Business_EndMin(int S_Business_EndMin) {
            this.S_Business_EndMin = S_Business_EndMin;
        }

        public void setS_BusinessRange(int S_BusinessRange) {
            this.S_BusinessRange = S_BusinessRange;
        }

        public void setS_MinMoney(double S_MinMoney) {
            this.S_MinMoney = S_MinMoney;
        }

        public void setS_Freight(int S_Freight) {
            this.S_Freight = S_Freight;
        }

        public void setS_YWCard_GUID(String S_YWCard_GUID) {
            this.S_YWCard_GUID = S_YWCard_GUID;
        }

        public void setS_YCShop_GUID(String S_YCShop_GUID) {
            this.S_YCShop_GUID = S_YCShop_GUID;
        }

        public void setS_Status(int S_Status) {
            this.S_Status = S_Status;
        }

        public String getS_GUID() {
            return S_GUID;
        }

        public String getS_Name() {
            return S_Name;
        }

        public String getS_HeadPhoto() {
            return S_HeadPhoto;
        }

        public double getS_Long() {
            return S_Long;
        }

        public double getS_Lat() {
            return S_Lat;
        }

        public float getS_Score() {
            return S_Score;
        }

        public int getS_CollectionNum() {
            return S_CollectionNum;
        }

        public boolean isS_IsBusiness() {
            return S_IsBusiness;
        }

        public int getS_Business_BeginHours() {
            return S_Business_BeginHours;
        }

        public int getS_Business_BeginMin() {
            return S_Business_BeginMin;
        }

        public int getS_Business_EndHours() {
            return S_Business_EndHours;
        }

        public int getS_Business_EndMin() {
            return S_Business_EndMin;
        }

        public int getS_BusinessRange() {
            return S_BusinessRange;
        }

        public double getS_MinMoney() {
            return S_MinMoney;
        }

        public int getS_Freight() {
            return S_Freight;
        }

        public String getS_YWCard_GUID() {
            return S_YWCard_GUID;
        }

        public String getS_YCShop_GUID() {
            return S_YCShop_GUID;
        }

        public int getS_Status() {
            return S_Status;
        }
    }
}
