package whcs.wohui.zz.Bean;

import android.content.Intent;

import java.io.Serializable;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/15 11:16
 * 版本：V1.0
 * 修改历史：
 */
public class DBgoodsData implements Serializable{
//    "(_id integer primary key autoincrement,SerialNumber text,NumberSM text,)"
//            +"NumberComm text,PriceUnit text,Integral text,CountCollected text,CountSaleTotal text,"
//            +"CountStockTotal text,NameComm text,BriefProduct text,UrlListImage text,OrderCount integer,";
    private String SerialNumber;
    private String NumberSM;
    private String NumberComm;
    private double PriceUnit;
    private int CountCollected;
    private int CountSaleTotal;
    private int CountStockTotal;
    private String NameComm;
    private String BriefProduct;
    private String UrlListImage;
    private double Integral;
    private int OrderCount;

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getNumberSM() {
        return NumberSM;
    }

    public void setNumberSM(String numberSM) {
        NumberSM = numberSM;
    }

    public String getNumberComm() {
        return NumberComm;
    }

    public void setNumberComm(String numberComm) {
        NumberComm = numberComm;
    }

    public double getPriceUnit() {
        return PriceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        PriceUnit = priceUnit;
    }

    public int getCountCollected() {
        return CountCollected;
    }

    public void setCountCollected(int countCollected) {
        CountCollected = countCollected;
    }

    public int getCountSaleTotal() {
        return CountSaleTotal;
    }

    public void setCountSaleTotal(int countSaleTotal) {
        CountSaleTotal = countSaleTotal;
    }

    public int getCountStockTotal() {
        return CountStockTotal;
    }

    public void setCountStockTotal(int countStockTotal) {
        CountStockTotal = countStockTotal;
    }

    public String getNameComm() {
        return NameComm;
    }

    public void setNameComm(String nameComm) {
        NameComm = nameComm;
    }

    public String getBriefProduct() {
        return BriefProduct;
    }

    public void setBriefProduct(String briefProduct) {
        BriefProduct = briefProduct;
    }

    public String getUrlListImage() {
        return UrlListImage;
    }

    public void setUrlListImage(String urlListImage) {
        UrlListImage = urlListImage;
    }

    public double getIntegral() {
        return Integral;
    }

    public void setIntegral(double integral) {
        Integral = integral;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    @Override
    public String toString() {
        return "DBgoodsData{" +
                "SerialNumber='" + SerialNumber + '\'' +
                ", NumberSM='" + NumberSM + '\'' +
                ", NumberComm='" + NumberComm + '\'' +
                ", PriceUnit='" + PriceUnit + '\'' +
                ", CountCollected=" + CountCollected +
                ", CountSaleTotal=" + CountSaleTotal +
                ", CountStockTotal=" + CountStockTotal +
                ", NameComm='" + NameComm + '\'' +
                ", BriefProduct='" + BriefProduct + '\'' +
                ", UrlListImage='" + UrlListImage + '\'' +
                ", Integral=" + Integral +
                ", OrderCount=" + OrderCount +
                '}';
    }
}
