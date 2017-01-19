package whcs.wohui.zz.url;

/**
 * 说明：联网参数
 * 作者：陈杰宇
 * 时间： 2016/3/7 13:43
 * 版本：V1.0
 * 修改历史：
 */
public class ParamsKey {

    /**
     * 登录接口
     */
    public static String Login_Name = "Name";
    public static String Login_Pwd = "Pwd";

    /**
     * 用户KEY
     */
    public static String userKey = "userKey";

    /**
     * 删除收货地址
     */
    public static String DelConsignee_SerialNumber = "SerialNumber";
    /**
     *获取商家信息，超市编号
     */
    public static String ShopSerialNumber="SMNo";
    /**
     * 获取附近商家
     */
    public static final String NearbyShop_Latitude = "lat";//纬度
    public static final String NearbyShop_Longitude = "lon";//经度
    public static final String NearbyShop_OrderKey = "orderKey";//排序方式
    public static final String NearbyShop_Distance = "distance";//范围
    /**
     * 添加收货地址
     */
    public static String AddConsignee_SerialNumber = "SerialNumber";
    public static String AddConsignee_NumberUser = "NumberUser";
    public static String AddConsignee_Consignee = "Consignee";
    public static String AddConsignee_Phone = "Phone";
    public static String AddConsignee_Province = "AddrProvince";
    public static String AddConsignee_City= "AddrCity";
    public static String AddConsignee_District = "AddrDistrict";
    public static String AddConsignee_Town = "AddrTown";
    public static String AddConsignee_AddrDetail = "AddrDetail";
    public static String AddConsignee_MapLongitude = "MapLongitude";
    public static String AddConsignee_MapLatitude = "MapLatitude";
    public static String AddConsignee_IsDefault = "IsDefault";

    /**
     * 获取订单列表
     * 未评价订单
     * 申请退货订单
     */
    public static String order_Page = "Page";
    public static String order_Rows = "Rows";

    /**
     * 删除订单
     */
    public static String DelOrder_OrderNumber = "OrderNumber";
    public static String DelOrder_status = "state";

    /**
     * 订单首次评价
     */
    public static String OrderCommentFirst_NumberSM = "NumberSM";
    public static String OrderCommentFirst_NumberOrder = "NumberOrder";
    public static String OrderCommentFirst_Score = "FirstScore";
    public static String OrderCommentFirst_Content = "FirstContentSubmit";

    /**
     * 订单追加评论
     */
    public static String OrderCommentSecond_serialNumber = "serialNumber";
    public static String OrderCommentSecond_second = "secondContentSubmit";

    /**
     * 获取订单详情
     */
    public static String OrderDetial_serialNumber = "serialNumber";

    /**
     * 确认收货
     */
    public static String ConfimGoods_serialNumber = "OrderNumber";
    public static String ConfimGoods_numberSM = "numberSM";
    public static String ConfimGoods_totalCommPrice = "totalCommPrice";
    public static String ConfimGoods_totalIntegral = "totalIntegral";
    public static String ConfimGoods_moneyActual = "moneyActual";
    /**
     * 获取商家评价列表
     */
    public  static final String CommentList_nowPage = "ShowPage";
    //超市编号，见上面超市编号  ShopSerialNumber
    /**
     * 获取商品列表
     */
    public static final String GetGoods_shopSerialNo = "SMNo";
    public static final String GetGoods_pageIndex = "pageIndex";
    public static final String GetGoods_pageSize = "pageSize";
    public static final String GetGoods_goodsOrder = "typeOrder";
    public static final String GetGoods_CateNo = "cateNo";
    /**
     * 搜索商品
     */
    public static final String GoodsSearch_searchName = "searchName";

    /**
     * 获取商品分类
     */
    public static final String GoodsSort_shopSerialNo = "SMNo";
    /**
     * 确认下单
     */
    public static final String CreatOrder_YWCardNumber = "YWCardNumber";//云网卡号
    public static final String CreatOrder_SMNo = "SMNo";//超市ID
    public static final String CreatOrder_AddrNo = "DA_GUID";//收货地址ID
    public static final String CreatOrder_Remark = "O_OtherMsg";//订单留言
    public static final String CreatOrder_ReceiptTime = "O_ArrivaTime";//运费
    public static final String CreatOrder_ReceiptMethod = "O_ReceivingMode";//收货方式
    /**
     * 获取在某一超市下的商品列表
     */
    public static final String ShopCartList_SMNo = "SMNo";//超市ID
    /**
     * 获取微信支付的预付单
     */
    public static final String GetWXPryId_attach ="attach";//附加信息
    public static final String GetWXPryId_body = "body";//商品信息
    public static final String GetWXPryId_detail= "detail";//商品详情
    public static final String GetWXPryId_orderNumber= "out_trade_no";//订单号
    /**
     * 清空后台购物车
     */
    public static final String ClearCart_SMNo = "SMNo";//超市ID
    /* =====================我会卡支付============================================= */
    public static String PAYORDER_ORDERNO = "orderNo";
    public static String PAYORDER_PWD = "payPwd";
    public static String PAYORDER_UID = "CardGUID";
    public static String PAYORDER_PAYMENT = "payMent";
    public static String PAYORDER_REMARK = "Remark";
    public static String PAYORDER_SOURCE = "Source";
    public static String PAYORDER_MONEY = "Money";
    public static String PAYORDER_SIGN = "Sign";
    /**
     * 获取订单评论的
     */
    public static final String GetOrderComment_orderNo = "orderNo";
    /**
     * 添加购物侧
     */
    public static final String AddCart_Json = "jsonMsg";
    public static final String AddCart_SMNo = "SMNo";
    /**
     * 申请退款
     */
    public static final String RefundRequest_OrderNumber = "OrderNumber";
    public static final String RefundRequest_Reason = "reason";
    /**
     * 获取对应商家的购物车列表
     */
    public static final String GetCart_SMNo = "SMNo";
}
