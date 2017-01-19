package whcs.wohui.zz.Bean;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/5 10:23
 * 版本：V1.0
 * 修改历史：
 */
public class ShopCollectData {
    private String SerialNumber;
    private String NameSM;
    private String AddrArea;
    private String AddrDetail;
    private String MapLongitude;
    private String MapLatitude;
    private int _id;

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getNameSM() {
        return NameSM;
    }

    public void setNameSM(String nameSM) {
        NameSM = nameSM;
    }

    public String getAddrArea() {
        return AddrArea;
    }

    public void setAddrArea(String addrArea) {
        AddrArea = addrArea;
    }

    public String getAddrDetail() {
        return AddrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        AddrDetail = addrDetail;
    }

    public String getMapLongitude() {
        return MapLongitude;
    }

    public void setMapLongitude(String mapLongitude) {
        MapLongitude = mapLongitude;
    }

    public String getMapLatitude() {
        return MapLatitude;
    }

    public void setMapLatitude(String mapLatitude) {
        MapLatitude = mapLatitude;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
