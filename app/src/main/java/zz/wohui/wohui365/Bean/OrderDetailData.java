package zz.wohui.zz.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/16 14:43
 * 版本：V1.0
 * 修改历史：
 */
public class OrderDetailData implements Serializable {

    public boolean isReceive() {
        return IsReceive;
    }

    public void setIsReceive(boolean isReceive) {
        IsReceive = isReceive;
    }

    /**
     * detail : [{"CS_Name":"鲁花5S压榨一级花生油1.8L","CS_MainImg":"/50_UploadFile/Product/201602201642206200.png","OD_GUID":"DFC6E8D408044DF3BC6492BC7B85E841","OD_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OD_CommGUID":"05c4987d95fd4baa98048d432eb3908c","OD_UnitPrice":0.01,"OD_BuyNum":1,"OD_TotalPrice":0.01,"OD_TotalXFJJ":0,"OD_TotalIntegral":10}]
     * O_GUID : E75F886C3FF5467E83B3B564BACEA4A0
     * O_UserGUID : 702876A0882E4E989CE1CFC867E64396
     * O_SMGUID : 4E1AFD6B196341E68C49C1B3AB07E30F
     * O_TotalPrice : 0.01
     * O_Freight : 0
     * O_TotalXFJJ : 0
     * O_TotalIntegral : 10
     * O_ReceivingMode : 10
     * O_ArrivaTime : 2017-01-13T16:10:04.977
     * O_OtherMsg :
     * O_PayMnet : 10
     * O_RealName : 赵志勇
     * O_Phone : 13803998721
     * O_Province : 河南省
     * O_City : 郑州市
     * O_District : 高新区
     * O_Towns : 莲花街
     * O_Address : 总部企业基地36栋我惠集团
     * O_Status : 70
     * O_CommentStatus : 10
     */
    private boolean IsReceive;
    private String O_GUID;
    private String O_UserGUID;
    private String O_SMGUID;
    private double O_TotalPrice;
    private double O_Freight;
    private double O_TotalXFJJ;
    private double O_TotalIntegral;
    private int O_ReceivingMode;
    private String O_ArrivaTime;
    private String O_OtherMsg;
    private int O_PayMnet;
    private String O_RealName;
    private String O_Phone;
    private String O_Province;
    private String O_City;
    private String O_District;
    private String O_Towns;
    private String O_Address;
    private int O_Status;
    private int O_CommentStatus;
    /**
     * CS_Name : 鲁花5S压榨一级花生油1.8L
     * CS_MainImg : /50_UploadFile/Product/201602201642206200.png
     * OD_GUID : DFC6E8D408044DF3BC6492BC7B85E841
     * OD_OrderGUID : E75F886C3FF5467E83B3B564BACEA4A0
     * OD_CommGUID : 05c4987d95fd4baa98048d432eb3908c
     * OD_UnitPrice : 0.01
     * OD_BuyNum : 1
     * OD_TotalPrice : 0.01
     * OD_TotalXFJJ : 0
     * OD_TotalIntegral : 10
     */

    private List<DetailEntity> detail;

    public void setO_GUID(String O_GUID) {
        this.O_GUID = O_GUID;
    }

    public void setO_UserGUID(String O_UserGUID) {
        this.O_UserGUID = O_UserGUID;
    }

    public void setO_SMGUID(String O_SMGUID) {
        this.O_SMGUID = O_SMGUID;
    }

    public void setO_TotalPrice(double O_TotalPrice) {
        this.O_TotalPrice = O_TotalPrice;
    }

    public void setO_Freight(double O_Freight) {
        this.O_Freight = O_Freight;
    }

    public void setO_TotalXFJJ(double O_TotalXFJJ) {
        this.O_TotalXFJJ = O_TotalXFJJ;
    }

    public void setO_TotalIntegral(double O_TotalIntegral) {
        this.O_TotalIntegral = O_TotalIntegral;
    }

    public void setO_ReceivingMode(int O_ReceivingMode) {
        this.O_ReceivingMode = O_ReceivingMode;
    }

    public void setO_ArrivaTime(String O_ArrivaTime) {
        this.O_ArrivaTime = O_ArrivaTime;
    }

    public void setO_OtherMsg(String O_OtherMsg) {
        this.O_OtherMsg = O_OtherMsg;
    }

    public void setO_PayMnet(int O_PayMnet) {
        this.O_PayMnet = O_PayMnet;
    }

    public void setO_RealName(String O_RealName) {
        this.O_RealName = O_RealName;
    }

    public void setO_Phone(String O_Phone) {
        this.O_Phone = O_Phone;
    }

    public void setO_Province(String O_Province) {
        this.O_Province = O_Province;
    }

    public void setO_City(String O_City) {
        this.O_City = O_City;
    }

    public void setO_District(String O_District) {
        this.O_District = O_District;
    }

    public void setO_Towns(String O_Towns) {
        this.O_Towns = O_Towns;
    }

    public void setO_Address(String O_Address) {
        this.O_Address = O_Address;
    }

    public void setO_Status(int O_Status) {
        this.O_Status = O_Status;
    }

    public void setO_CommentStatus(int O_CommentStatus) {
        this.O_CommentStatus = O_CommentStatus;
    }

    public void setDetail(List<DetailEntity> detail) {
        this.detail = detail;
    }

    public String getO_GUID() {
        return O_GUID;
    }

    public String getO_UserGUID() {
        return O_UserGUID;
    }

    public String getO_SMGUID() {
        return O_SMGUID;
    }

    public double getO_TotalPrice() {
        return O_TotalPrice;
    }

    public double getO_Freight() {
        return O_Freight;
    }

    public double getO_TotalXFJJ() {
        return O_TotalXFJJ;
    }

    public double getO_TotalIntegral() {
        return O_TotalIntegral;
    }

    public int getO_ReceivingMode() {
        return O_ReceivingMode;
    }

    public String getO_ArrivaTime() {
        return O_ArrivaTime;
    }

    public String getO_OtherMsg() {
        return O_OtherMsg;
    }

    public int getO_PayMnet() {
        return O_PayMnet;
    }

    public String getO_RealName() {
        return O_RealName;
    }

    public String getO_Phone() {
        return O_Phone;
    }

    public String getO_Province() {
        return O_Province;
    }

    public String getO_City() {
        return O_City;
    }

    public String getO_District() {
        return O_District;
    }

    public String getO_Towns() {
        return O_Towns;
    }

    public String getO_Address() {
        return O_Address;
    }

    public int getO_Status() {
        return O_Status;
    }

    public int getO_CommentStatus() {
        return O_CommentStatus;
    }

    public List<DetailEntity> getDetail() {
        return detail;
    }

    public static class DetailEntity implements Serializable{
        private String CS_Name;
        private String CS_MainImg;
        private String OD_GUID;
        private String OD_OrderGUID;
        private String OD_CommGUID;
        private double OD_UnitPrice;
        private int OD_BuyNum;
        private double OD_TotalPrice;
        private double OD_TotalXFJJ;
        private double OD_TotalIntegral;

        public void setCS_Name(String CS_Name) {
            this.CS_Name = CS_Name;
        }

        public void setCS_MainImg(String CS_MainImg) {
            this.CS_MainImg = CS_MainImg;
        }

        public void setOD_GUID(String OD_GUID) {
            this.OD_GUID = OD_GUID;
        }

        public void setOD_OrderGUID(String OD_OrderGUID) {
            this.OD_OrderGUID = OD_OrderGUID;
        }

        public void setOD_CommGUID(String OD_CommGUID) {
            this.OD_CommGUID = OD_CommGUID;
        }

        public void setOD_UnitPrice(double OD_UnitPrice) {
            this.OD_UnitPrice = OD_UnitPrice;
        }

        public void setOD_BuyNum(int OD_BuyNum) {
            this.OD_BuyNum = OD_BuyNum;
        }

        public void setOD_TotalPrice(double OD_TotalPrice) {
            this.OD_TotalPrice = OD_TotalPrice;
        }

        public void setOD_TotalXFJJ(double OD_TotalXFJJ) {
            this.OD_TotalXFJJ = OD_TotalXFJJ;
        }

        public void setOD_TotalIntegral(double OD_TotalIntegral) {
            this.OD_TotalIntegral = OD_TotalIntegral;
        }

        public String getCS_Name() {
            return CS_Name;
        }

        public String getCS_MainImg() {
            return CS_MainImg;
        }

        public String getOD_GUID() {
            return OD_GUID;
        }

        public String getOD_OrderGUID() {
            return OD_OrderGUID;
        }

        public String getOD_CommGUID() {
            return OD_CommGUID;
        }

        public double getOD_UnitPrice() {
            return OD_UnitPrice;
        }

        public int getOD_BuyNum() {
            return OD_BuyNum;
        }

        public double getOD_TotalPrice() {
            return OD_TotalPrice;
        }

        public double getOD_TotalXFJJ() {
            return OD_TotalXFJJ;
        }

        public double getOD_TotalIntegral() {
            return OD_TotalIntegral;
        }
    }
}
