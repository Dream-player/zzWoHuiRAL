package whcs.wohui.zz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import okhttp3.Call;
import whcs.wohui.zz.Bean.OrderDetailBean;
import whcs.wohui.zz.Bean.OrderDetailData;
import whcs.wohui.zz.Bean.ShopDetailBean;
import whcs.wohui.zz.adapter.OrderListItemGoodsAdapter;
import whcs.wohui.zz.callback.OrderDetailCallBack;
import whcs.wohui.zz.callback.ShopDetailCallBack;
import whcs.wohui.zz.myview.MyListViewInListView;
import whcs.wohui.zz.myview.OrderTimeActiveView;
import whcs.wohui.zz.url.ParamsKey;
import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.utils.MyRequestParams;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：订单详情页面
 * 作者：陈杰宇
 * 时间： 2016/2/16 17:10
 * 版本：V1.0
 * 修改历史：
 */
public class OrderDetailsActivity extends BaseActivity {
    private static final String TAG = "OrderDetailsActivity";
    private TextView tvOrderStatus;
    private TextView tvOrderStatus2;
    private TextView tvOrderPrice;
    private TextView tvShopName;
//    private TextView tvGoodsName;
    private TextView tvOrderAddress;
    private TextView tvDeliveryWay;
    private TextView tvPayWay;
    private TextView tvPayMoney;
    private TextView tvOrderCreateTime;
    private Button btnConnectShop;
    private RelativeLayout goBack;
    private TextView tvTitleName;
    private MyListViewInListView lvOrderGoods;
    private OrderTimeActiveView myVStatus;
    private static final int GetOrderDetail_SUCCESS =5115;//得到订单详情的handler.what
    private static final int GetShopName_SUCCESS = 11234;//得到超市名称的handler.what
    private Context ctx;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;

    private void assignViews() {
        btnConnectShop = (Button) findViewById(R.id.btn_connect_shop);
        tvTitleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        tvOrderStatus2 = (TextView) findViewById(R.id.tvOrderStatus2);
        tvOrderPrice = (TextView) findViewById(R.id.tvOrderPrice);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
//        tvGoodsName = (TextView) findViewById(R.id.tvGoodsName);
        tvOrderAddress = (TextView) findViewById(R.id.tvOrderAddress);
        tvDeliveryWay = (TextView) findViewById(R.id.tvDeliveryWay);
        tvPayWay = (TextView) findViewById(R.id.tvPayWay);
        tvPayMoney = (TextView) findViewById(R.id.tvPayMoney);
        tvOrderCreateTime = (TextView) findViewById(R.id.tvOrderCreateTime);
        myVStatus=(OrderTimeActiveView)findViewById(R.id.myVStatus);
        lvOrderGoods = (MyListViewInListView) findViewById(R.id.lv_order_goods);
    }
    private OrderDetailData bean=null;
    private String shopPhone ;
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail);
        assignViews();
        initData();
        initView();
        initListener();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailsActivity.this.onBackPressed();
                finish();
            }
        });

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        btnConnectShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG,"shopphone:"+shopPhone);
                if ("".equals(shopPhone)||shopPhone==null){
                    showToast(ctx,"该商家暂无联系方式!");
                }else {
                    String tel = "tel:"+shopPhone;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ctx = this;
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        Intent intent=getIntent();
        String from = intent.getStringExtra("from");
        if(from.equals("WXPayEntryActivity")){
            SharedPreferences mySharedPreferences = getSharedPreferences(
                    getMyApplication().getUserKey(), Activity.MODE_PRIVATE);
            String orderNumber = mySharedPreferences.getString("orderNumber","");
            //通过orderNumber进行网络请求得到订单详情数据
            getOrderDetail(orderNumber);
        }else if (from.equals("OrderFragment")){
            bean= (OrderDetailData)
                    intent.getSerializableExtra("OrderRowEntity");
            String shopNumber = bean.getO_SMGUID();
            getShopName(shopNumber);//通过超市编号获得超市名称
            setView(bean);
        }

    }
    /**
     * 得到订单详情
     * @param orderNumber 订单列表
     */
    private void getOrderDetail(String orderNumber) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.OrderDetial_serialNumber, orderNumber);
        params.addStringRequest(ParamsKey.userKey,getMyApplication().getUserKey());
        String strUrl = Urls.GetOrderDetail;
        myOkHttpUtils.postRequest(strUrl, params, new OrderDetailCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, "网络出错：" + e.toString());
                dismissDialog();
            }

            @Override
            public void onResponse(OrderDetailBean response) {
                int state = response.getState();
                if (state == 1) {
                    //得到单个订单实体
                    OrderDetailData data = response.getData();
                    Message msg = new Message();
                    msg.what = GetOrderDetail_SUCCESS;
                    msg.obj = data;
                    handler.sendMessage(msg);
                } else if (state == 0) {
                    showToast(ctx, "数据为空，" + response.getMsg());
                    dismissDialog();
                } else if (state == -1) {
                    showToast(ctx, "发生异常，" + response.getMsg());
                    dismissDialog();
                }
            }
        });
    }

    /**
     * 得到超市名称
     * @param shopNumber
     */
    private void getShopName(String shopNumber){
        showDialog(ctx);
        MyRequestParams params = new MyRequestParams();
        params.addStringRequest(ParamsKey.userKey,getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.ShopSerialNumber,shopNumber);
        String strUrl = Urls.GetShopDetail;
        myOkHttpUtils.postRequest(strUrl, params, new ShopDetailCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, "网络出错：" + e.toString());
                dismissDialog();
            }

            @Override
            public void onResponse(ShopDetailBean response) {
                int state = response.getState();
                if (state == 1) {
                    //得到单个订单实体
                    ShopDetailBean.DataBean shopDetail = response.getData();
                    Message msg = new Message();
                    msg.what = GetShopName_SUCCESS;
                    msg.obj = shopDetail;
                    handler.sendMessage(msg);
                } else if (state == 0) {
                    showToast(ctx, "数据为空，" + response.getMsg());
                    dismissDialog();
                } else if (state == -1) {
                    showToast(ctx, "发生异常，" + response.getMsg());
                    dismissDialog();
                }
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetOrderDetail_SUCCESS:
                    dismissDialog();
                    OrderDetailData bean = (OrderDetailData) msg.obj;
                    setView(bean);
                    getShopName(bean.getO_SMGUID());//通过超市编号获得超市名称
                    break;
                case GetShopName_SUCCESS:
                    ShopDetailBean.DataBean shopDetail= (ShopDetailBean.DataBean) msg.obj;
                    dismissDialog();
                    tvShopName.setText(shopDetail.getS_Name());
                    shopPhone = shopDetail.getS_LinkMan_Phone();
                    break;
            }
        }
    };

    /**
     * 初始化界面
     */
    private void initView() {
        tvTitleName.setText("订单详情");
    }

    /**
     * 传入订单实体类，来设置界面数据展示
     * @param bean 订单实体类
     */
    private void setView (OrderDetailData bean){
        OrderListItemGoodsAdapter goodsAdapter =
                new OrderListItemGoodsAdapter(bean.getDetail(),OrderDetailsActivity.this);
        //设置收货地址
        tvOrderAddress.setText(bean.getO_District()
                                + bean.getO_Towns()
                                +bean.getO_Address());
        lvOrderGoods.setAdapter(goodsAdapter);
        LogUtils.e(TAG,"订单状态:"+bean.getO_Status()+bean.getO_RealName());
        myVStatus.setOrderActive(bean.getO_Status());
        tvOrderPrice.setText("¥" + bean.getO_TotalPrice());
//        tvOrderStatus2.setText(bean.getO_Status()+"");
        LogUtils.e(TAG+"*******"+bean.getO_Status());
        List<OrderDetailData.DetailEntity> commList=
                bean.getDetail();
        StringBuilder stringBuilder=new StringBuilder();
        for (OrderDetailData.DetailEntity commListEntity:commList){
            stringBuilder.append(commListEntity.getCS_Name()+"、");
        }
//        tvGoodsName.setText(stringBuilder.toString());
        if(bean.getO_ReceivingMode()==10){
            tvDeliveryWay.setText("自提");
        }else if (bean.getO_ReceivingMode()==20){
            tvDeliveryWay.setText("送货上门");
        }
        int payType = bean.getO_PayMnet();
        String payWay = "尚未付款";
        if(payType==10){
            payWay="我惠币支付";
        }else if (payType==20){
            payWay="微信支付";
        }else if (payType==30){
            payWay="支付宝支付";
        }else if (payType==40){
            payWay="消费奖金支付";
        }
        tvPayWay.setText(payWay);//付款方式，返回值为int
        tvPayMoney.setText("¥" + bean.getO_TotalPrice());
        tvOrderCreateTime.setText(bean.getO_ArrivaTime());
      //tvShopName.setText(bean.gets);
    }
}
