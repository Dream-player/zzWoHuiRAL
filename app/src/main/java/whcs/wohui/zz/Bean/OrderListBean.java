package whcs.wohui.zz.Bean;

import java.util.List;

/**
 * 说明：订单列表的Bean
 * 作者：陈杰宇
 * 时间： 2016/3/11 20:27
 * 版本：V1.0
 * 修改历史：
 */
public class OrderListBean {


    /**
     * State : 1
     * Data : {"PageIndex":1,"RowsCount":1,"DataCount":15,"PageCount":15,"data":[{"detail":[{"CS_Name":"鲁花5S压榨一级花生油1.8L","CS_MainImg":"/50_UploadFile/Product/201602201642206200.png","OD_GUID":"DFC6E8D408044DF3BC6492BC7B85E841","OD_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OD_CommGUID":"05c4987d95fd4baa98048d432eb3908c","OD_UnitPrice":0.01,"OD_BuyNum":1,"OD_TotalPrice":0.01,"OD_TotalXFJJ":0,"OD_TotalIntegral":10}],"O_GUID":"E75F886C3FF5467E83B3B564BACEA4A0","O_UserGUID":"702876A0882E4E989CE1CFC867E64396","O_SMGUID":"4E1AFD6B196341E68C49C1B3AB07E30F","O_TotalPrice":0.01,"O_Freight":0,"O_TotalXFJJ":0,"O_TotalIntegral":10,"O_ReceivingMode":10,"O_ArrivaTime":"2016-09-01T16:10:04.977","O_OtherMsg":"","O_PayMnet":10,"O_RealName":"赵志勇","O_Phone":"13803998721","O_Province":"河南省","O_City":"郑州市","O_District":"高新区","O_Towns":"莲花街","O_Address":"总部企业基地36栋我惠集团","O_Status":70,"O_CommentStatus":10}]}
     * Msg :
     */

    private int State;
    /**
     * PageIndex : 1
     * RowsCount : 1
     * DataCount : 15
     * PageCount : 15
     * data : [{"detail":[{"CS_Name":"鲁花5S压榨一级花生油1.8L","CS_MainImg":"/50_UploadFile/Product/201602201642206200.png","OD_GUID":"DFC6E8D408044DF3BC6492BC7B85E841","OD_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OD_CommGUID":"05c4987d95fd4baa98048d432eb3908c","OD_UnitPrice":0.01,"OD_BuyNum":1,"OD_TotalPrice":0.01,"OD_TotalXFJJ":0,"OD_TotalIntegral":10}],"O_GUID":"E75F886C3FF5467E83B3B564BACEA4A0","O_UserGUID":"702876A0882E4E989CE1CFC867E64396","O_SMGUID":"4E1AFD6B196341E68C49C1B3AB07E30F","O_TotalPrice":0.01,"O_Freight":0,"O_TotalXFJJ":0,"O_TotalIntegral":10,"O_ReceivingMode":10,"O_ArrivaTime":"2016-09-01T16:10:04.977","O_OtherMsg":"","O_PayMnet":10,"O_RealName":"赵志勇","O_Phone":"13803998721","O_Province":"河南省","O_City":"郑州市","O_District":"高新区","O_Towns":"莲花街","O_Address":"总部企业基地36栋我惠集团","O_Status":70,"O_CommentStatus":10}]
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
        private int PageIndex;
        private int RowsCount;
        private int DataCount;
        private int PageCount;
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
         * O_Status : 70
         * O_CommentStatus : 10
         */

        private List<OrderDetailData> data;

        public void setPageIndex(int PageIndex) {
            this.PageIndex = PageIndex;
        }

        public void setRowsCount(int RowsCount) {
            this.RowsCount = RowsCount;
        }

        public void setDataCount(int DataCount) {
            this.DataCount = DataCount;
        }

        public void setPageCount(int PageCount) {
            this.PageCount = PageCount;
        }

        public void setData(List<OrderDetailData> data) {
            this.data = data;
        }

        public int getPageIndex() {
            return PageIndex;
        }

        public int getRowsCount() {
            return RowsCount;
        }

        public int getDataCount() {
            return DataCount;
        }

        public int getPageCount() {
            return PageCount;
        }

        public List<OrderDetailData> getData() {
            return data;
        }


    }
}
