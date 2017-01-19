package whcs.wohui.zz.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 说明：收货地址的Bean
 * 作者：陈杰宇
 * 时间： 2016/3/9 15:22
 * 版本：V1.0
 * 修改历史：
 */
public class AddressBean {


    /**
     * State : 1
     * Data : [{"DA_GUID":"46A200D047D24E3E8F536D332B0E97A2","DA_UserGUID":"702876A0882E4E989CE1CFC867E64396","DA_RealName":"赵志勇","DA_Phone":"13803998721","DA_Province":"河南省","DA_City":"郑州市","DA_District":"高新区","DA_Towns":"莲花街","DA_Address":"总部企业基地36栋我惠集团","DA_Long":113.572287,"DA_Lat":34.824239,"DA_IsDefault":true},{"DA_GUID":"C7158B92B5114AAC8B42B2853707860D","DA_UserGUID":"702876A0882E4E989CE1CFC867E64396","DA_RealName":"1","DA_Phone":"1","DA_Province":"天津市","DA_City":"天津市","DA_District":"和平区","DA_Towns":"1","DA_Address":"12","DA_Long":1,"DA_Lat":1,"DA_IsDefault":false}]
     * Msg :
     */

    private int State;
    private String Msg;
    /**
     * DA_GUID : 46A200D047D24E3E8F536D332B0E97A2
     * DA_UserGUID : 702876A0882E4E989CE1CFC867E64396
     * DA_RealName : 赵志勇
     * DA_Phone : 13803998721
     * DA_Province : 河南省
     * DA_City : 郑州市
     * DA_District : 高新区
     * DA_Towns : 莲花街
     * DA_Address : 总部企业基地36栋我惠集团
     * DA_Long : 113.572287
     * DA_Lat : 34.824239
     * DA_IsDefault : true
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

    public static class DataEntity implements Serializable{
        private String DA_GUID;
        private String DA_UserGUID;
        private String DA_RealName;
        private String DA_Phone;
        private String DA_Province;
        private String DA_City;
        private String DA_District;
        private String DA_Towns;
        private String DA_Address;
        private double DA_Long;
        private double DA_Lat;
        private boolean DA_IsDefault;

        public void setDA_GUID(String DA_GUID) {
            this.DA_GUID = DA_GUID;
        }

        public void setDA_UserGUID(String DA_UserGUID) {
            this.DA_UserGUID = DA_UserGUID;
        }

        public void setDA_RealName(String DA_RealName) {
            this.DA_RealName = DA_RealName;
        }

        public void setDA_Phone(String DA_Phone) {
            this.DA_Phone = DA_Phone;
        }

        public void setDA_Province(String DA_Province) {
            this.DA_Province = DA_Province;
        }

        public void setDA_City(String DA_City) {
            this.DA_City = DA_City;
        }

        public void setDA_District(String DA_District) {
            this.DA_District = DA_District;
        }

        public void setDA_Towns(String DA_Towns) {
            this.DA_Towns = DA_Towns;
        }

        public void setDA_Address(String DA_Address) {
            this.DA_Address = DA_Address;
        }

        public void setDA_Long(double DA_Long) {
            this.DA_Long = DA_Long;
        }

        public void setDA_Lat(double DA_Lat) {
            this.DA_Lat = DA_Lat;
        }

        public void setDA_IsDefault(boolean DA_IsDefault) {
            this.DA_IsDefault = DA_IsDefault;
        }

        public String getDA_GUID() {
            return DA_GUID;
        }

        public String getDA_UserGUID() {
            return DA_UserGUID;
        }

        public String getDA_RealName() {
            return DA_RealName;
        }

        public String getDA_Phone() {
            return DA_Phone;
        }

        public String getDA_Province() {
            return DA_Province;
        }

        public String getDA_City() {
            return DA_City;
        }

        public String getDA_District() {
            return DA_District;
        }

        public String getDA_Towns() {
            return DA_Towns;
        }

        public String getDA_Address() {
            return DA_Address;
        }

        public double getDA_Long() {
            return DA_Long;
        }

        public double getDA_Lat() {
            return DA_Lat;
        }

        public boolean isDA_IsDefault() {
            return DA_IsDefault;
        }
    }
}
