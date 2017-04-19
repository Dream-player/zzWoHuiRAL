package whcs.wohui.zz.url;

/**
 * 说明：联网字符串
 * 作者：陈杰宇
 * 时间： 2016/3/7 13:40
 * 版本：V1.0
 * 修改历史：
 */
public class Urls {

//    public static String Base_Url = "http://192.168.0.233:90/API/WS_Supermarket.asmx/";
    public static String Base_Url = "http://yc.wohui365.com/API/WS_Supermarket.asmx/";
//    public static String Base_Url = "http://192.168.0.12:8003/API/WS_Supermarket.asmx/";
    //登录
    public static String Login = Base_Url + "Login";
    //获取用户信息
    public static String GetUserInfo = Base_Url + "GetUserInfo";
    //获取收货地址列表
    public static String GetConsigneeList = Base_Url + "GetAddrList";
    //删除地址
    public static String DelConsignee = Base_Url + "DeleteConsignee";
    //添加收货地址
    public static String AddConsignee = Base_Url + "AddConsignee";
    //修改收货地址
    public static String EditConsignee = Base_Url + "EditConsignee";
    //获取全部订单
    public static String GetOrderList = Base_Url + "GetOrderList";
    //获取未评价订单
    public static String GetOrderListForNoEvaluation = Base_Url + "GetOrderListForNoEvaluation";
    //获取申请退货订单
    public static String GetOrderListForReturnGoods = Base_Url + "GetOrderListForReturnGoods";
    //删除订单
    public static String DelOrder = Base_Url + "DelOrder";
    //订单首次评价
    public static String AddOrderCommentFirst = Base_Url + "AddOrderCommentFirst";
    //订单追加评价
    public static String AddOrderCommentSecond = Base_Url + "AddOrderCommentSecond";
    //获取订单详情
    public static String GetOrderDetail = Base_Url + "GetOrderDetial";
    //确认收货
    public static String ConfimGoodsReceipt = Base_Url + "ConfimGoodsReceipt";
    //获取商家信息
    public static String GetShopDetail=Base_Url + "GetBusinessInfo";
    //获取附近商家
    public  static final String GetNearbyShop = Base_Url+"GetBusiness";
    //获取评价列表
    public static final String GetCommentList = Base_Url+"GetBusinessComment";
    public static final String GetGoodsList = Base_Url + "SMCommList";
    //搜索商品
    public static final String GoodsSearch = Base_Url + "SearchSMCommList";
    //获取商品类别
    public static final String GetGoodsSort = Base_Url+"SMCateList";
    //提交购物车
    public static final String ConfirmCart = Base_Url+"AddCart";
    //获取购物车列表
    public static final String ShopCartList = Base_Url+"GetCart";
    //确认下单
    public static final String CreatOrder = Base_Url+"SureCart";
    //微信支付，获取预付单接口
    public static final String GetWXPryId = Base_Url + "WeChatPay";
    //支付宝异步通知结果
    public static final String AliPayNotice = "http://101.201.38.189:84/AliPayNotice.aspx";
    //清空后台购物车接口
    public static final String ClearCart = Base_Url + "ClearCart";
    //我惠卡支付接口
    public static final String WHKPAY = Base_Url + "PayOrder";
    //获取订单的评论内容
    public static final String GetOrderComment = Base_Url + "GetUserComment";
    //获取头部轮播图列表
    public static final String GetAdList = Base_Url + "AdList";
    //申请退款
    public static final String RefundRequest = Base_Url+"ApplyReceive";
    //取消订单
    public static final String CancelOrder = Base_Url+"CancelOrder";
    //跟新接口
    public static final String UpdateApp = Base_Url + "UpdateApp";

    public static final String NearSMCommList = Base_Url + "NearSMCommList";


}
