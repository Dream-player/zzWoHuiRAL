package whcs.wohui.zz.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/11 9:28
 * 版本：V1.0
 * 修改历史：
 */
public class OrderDetailBean {

    /**
     * State : 1
     * Data : {"detail":[{"CS_Name":"鲁花5S压榨一级花生油1.8L","CS_MainImg":"http://files.wohuiyun.com/yunchao.files/Product/201602201642206200.png","OD_GUID":"DFC6E8D408044DF3BC6492BC7B85E841","OD_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OD_CommGUID":"05c4987d95fd4baa98048d432eb3908c","OD_UnitPrice":0.01,"OD_BuyNum":1,"OD_TotalPrice":0.01,"OD_TotalXFJJ":0,"OD_TotalIntegral":10}],"O_GUID":"E75F886C3FF5467E83B3B564BACEA4A0","O_UserGUID":"702876A0882E4E989CE1CFC867E64396","O_SMGUID":"4E1AFD6B196341E68C49C1B3AB07E30F","O_TotalPrice":0.01,"O_Freight":0,"O_TotalXFJJ":0,"O_TotalIntegral":10,"O_ReceivingMode":10,"O_ArrivaTime":"2016-09-01T16:10:04.977","O_OtherMsg":"","O_PayMnet":10,"O_RealName":"赵志勇","O_Phone":"13803998721","O_Province":"河南省","O_City":"郑州市","O_District":"高新区","O_Towns":"莲花街","O_Address":"总部企业基地36栋我惠集团","O_Status":50,"O_CommentStatus":10}
     * Msg :
     */

    private int State;
    /**
     * detail : [{"CS_Name":"鲁花5S压榨一级花生油1.8L","CS_MainImg":"http://files.wohuiyun.com/yunchao.files/Product/201602201642206200.png","OD_GUID":"DFC6E8D408044DF3BC6492BC7B85E841","OD_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OD_CommGUID":"05c4987d95fd4baa98048d432eb3908c","OD_UnitPrice":0.01,"OD_BuyNum":1,"OD_TotalPrice":0.01,"OD_TotalXFJJ":0,"OD_TotalIntegral":10}]
     * O_GUID : E75F886C3FF5467E83B3B564BACEA4A0
     * O_UserGUID : 702876A0882E4E989CE1CFC867E64396
     * O_SMGUID : 4E1AFD6B196341E68C49C1B3AB07E30F
     * O_TotalPrice : 0.01
     * O_Freight : 0
     * O_TotalXFJJ : 0
     * O_TotalIntegral : 10
     * O_ReceivingMode : 10
     * O_ArrivaTime : 2016-09-01T16:10:04.977
     * O_OtherMsg :
     * O_PayMnet : 10
     * O_RealName : 赵志勇
     * O_Phone : 13803998721
     * O_Province : 河南省
     * O_City : 郑州市
     * O_District : 高新区
     * O_Towns : 莲花街
     * O_Address : 总部企业基地36栋我惠集团
     * O_Status : 50
     * O_CommentStatus : 10
     */

    private OrderDetailData Data;
    private String Msg;

    public void setState(int State) {
        this.State = State;
    }

    public void setData(OrderDetailData Data) {
        this.Data = Data;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getState() {
        return State;
    }

    public OrderDetailData getData() {
        return Data;
    }

    public String getMsg() {
        return Msg;
    }

}
