package zz.wohui.wohui365.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;

import java.util.Random;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.NormalBean;
import zz.wohui.wohui365.Bean.OrderDetailBean;
import zz.wohui.wohui365.Bean.OrderDetailData;
import zz.wohui.wohui365.Bean.WXPrepayIDBean;
import zz.wohui.wohui365.Bean.WXPrepayIDBean.DataEntity;
import zz.wohui.wohui365.callback.NormalCallBack;
import zz.wohui.wohui365.callback.OrderDetailCallBack;
import zz.wohui.wohui365.callback.WXPrepayIDCallBack;
import zz.wohui.wohui365.myview.MyPayMethodLinearLayout;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MD5Encoder;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.utils.payutil.AliPayUtil;
import zz.wohui.wohui365.R;

/**
 * 说明：订单支付
 * 作者：陈杰宇
 * 时间： 2016/3/2 17:22
 * 版本：V1.0
 * 修改历史：
 */
public class PayOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvOrderPrice;
    private MyPayMethodLinearLayout payMethodWX;
    private MyPayMethodLinearLayout payMethodAL;
    private MyPayMethodLinearLayout payMethodWH;
    private MyPayMethodLinearLayout payMethodXFJJ;
    private TextView tvOrderIntegral;
    private EditText etPassword;
    private TextView payOrder;
    private TextView titleName;
    private RelativeLayout goBack;
    private Context ctx;
    private String payMethod = "";
    private IWXAPI api;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private String orderNumber;
    private String userKey;
    private static final int GetOrderDetail_SUCCESS =1995;
    private void assignViews() {
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvOrderIntegral = (TextView) findViewById(R.id.tvOrderIntegral);
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        tvOrderPrice = (TextView) findViewById(R.id.tvOrderPrice);
        payMethodWX = (MyPayMethodLinearLayout) findViewById(R.id.payMethodWX);
        payMethodAL = (MyPayMethodLinearLayout) findViewById(R.id.payMethodAL);
        payMethodWH = (MyPayMethodLinearLayout) findViewById(R.id.payMethodWH);
        payMethodXFJJ = (MyPayMethodLinearLayout) findViewById(R.id.payMethodXFJJ);

        payOrder = (TextView) findViewById(R.id.payOrder);
    }
    private int payType = 0;//支付方式
    private static final int PAY_TYPE_NULL = 0;//未选择支付方式
    private static final int PAY_TYPE_WX = 10;//微信支付
    private static final int PAY_TYPE_WH = 20;//支付宝支付
    private static final int PAY_TYPE_AL = 30;//我惠卡支付
    private static final int PAY_TYPE_XFJJ = 40;//消费奖金支付
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay_order);
        assignViews();//加载布局
        initData();//初始化数据
        initView();//初始化界面
        initListener();//初始化监听
        getOrderDetail(orderNumber);
    }

    /**
     * 得到订单详情
     * @param orderNumber 订单列表
     */
    private void getOrderDetail(String orderNumber) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.OrderDetial_serialNumber, orderNumber);
        params.addStringRequest(ParamsKey.userKey, userKey);
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
    private static final int ACTIVITY_FINISHI = 692;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetOrderDetail_SUCCESS:
                    OrderDetailData bean = (OrderDetailData) msg.obj;
                    setView(bean);
                    dismissDialog();
                  break;
                case ACTIVITY_FINISHI:

                    PayOrderActivity.this.finish();
                    break;
            }
        }
    };

    /**
     * 设置商品总价，与总积分
     * @param bean
     */
    private void setView(OrderDetailData bean) {
        tvOrderIntegral.setText(bean.getO_TotalXFJJ() + "");
        tvOrderPrice.setText(bean.getO_TotalPrice() + "");
    }

    //初始化数据
    private void initData() {
        ctx = this;
        userKey = getMyApplication().getUserKey();
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        Intent intent = getIntent();
        orderNumber = intent.getStringExtra("orderNumber");
        //把订单号储存在SharedPreferences中
        //其名字是用户的令牌
        SharedPreferences mySharedPreferences = getSharedPreferences(
                userKey, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("orderNumber", orderNumber);
        editor.commit();
    }

    private void initView() {
        titleName.setText("订单支付");
        payOrder.setEnabled(false);
    }

    private void initListener() {
        payMethodAL.setOnClickListener(this);
        payMethodWH.setOnClickListener(this);
        payMethodWX.setOnClickListener(this);
        payMethodXFJJ.setOnClickListener(this);
        payOrder.setOnClickListener(this);
        goBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payMethodAL:
                selectPayMethod("AL");
                break;
            case R.id.payMethodWH:
                selectPayMethod("WH");
                break;
            case R.id.payMethodWX:
                selectPayMethod("WX");
                break;
            case R.id.payMethodXFJJ:
                selectPayMethod("XFJJ");
                break;
            case R.id.payOrder:
                LogUtils.outLog("订单支付");
                payOrder(payType);
                break;

            case R.id.titleGoBack:
                this.onBackPressed();
                finish();
                break;
        }
    }

    /**
     * 支付订单
     */
    private void payOrder(int payType) {
        switch (payType){
            case PAY_TYPE_NULL://没有选择支付方式
                showToast(ctx, "请选择支付方式");
                break;
            case PAY_TYPE_WX://微信支付
                api = WXAPIFactory.createWXAPI(ctx,Constants.APP_ID,false);//得到微信api
                api.registerApp(Constants.APP_ID);//注册到微信
                if(!api.isWXAppInstalled()) {
                    showToast(ctx,"没有安装微信");
                    return;
                }
                if(!api.isWXAppSupportAPI())
                {
                    showToast(ctx,"微信版本过低,或者微信没有运行!");
                    LogUtils.e("微信版本过低，不支持微信支付");
                    return;
                }
                //
//                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                getPrepayId();
                break;
            case PAY_TYPE_AL://支付宝支付
                AliPayUtil myUtil = new AliPayUtil(PayOrderActivity.this);
                myUtil.pay(orderNumber);
                break;
            case PAY_TYPE_WH://我惠卡支付
                //我会卡支付
                String pwd = etPassword.getText().toString().trim();
                if (pwd.equals("")) {
                    showToast(ctx, "请输入密码");
                    return;
                }
                payOrder(orderNumber, pwd,PAY_TYPE_WH);
                break;
            case PAY_TYPE_XFJJ://我惠卡支付
                //我会卡支付
                 pwd = etPassword.getText().toString().trim();
                if (pwd.equals("")) {
                    showToast(ctx, "请输入密码");
                    return;
                }
                payOrder(orderNumber, pwd,PAY_TYPE_XFJJ);
                break;
        }
    }

    /**
     *
     * @param orderNumber
     * @param pwd
     * @param payType 支付方式,需转换
     */
    private void payOrder(String orderNumber,String pwd,int payType){
        payOrder.setClickable(false);
        params.clear();
        if (payType==PAY_TYPE_XFJJ){
            params.addStringRequest(ParamsKey.PAYORDER_PAYMENT,"XFJJPAY");
        }else {
            params.addStringRequest(ParamsKey.PAYORDER_PAYMENT,"JHBPAY");
        }
        params.addStringRequest(ParamsKey.userKey,getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.PAYORDER_ORDERNO, orderNumber);
        params.addStringRequest(ParamsKey.PAYORDER_PWD, pwd);
        String strUrl =Urls.WHKPAY;
        myOkHttpUtils.postRequest(strUrl, params, new NormalCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                payOrder.setClickable(true);
                showToast(ctx,MyUtils.networkError);
            }
            @Override
            public void onResponse(NormalBean response) {
                if (response.getState()==1){
                    payOrder.setClickable(true);
                    showToast(ctx, "支付成功");
                    Intent intent = new Intent(ctx,OrderDetailsActivity.class);
                    intent.putExtra("from","WXPayEntryActivity");
                    ctx.startActivity(intent);
                    PayOrderActivity.this.finish();
                }else {
                    showToast(ctx,"支付失败:"+response.getMsg());
                    payOrder.setClickable(true);
                }
            }
        });
    }
//    private void payOrder(String pwd, String payMent) {
//        params.clear();
//        params.addStringRequest(ParamsKey.PAYORDER_UID, getMyApplication().getUserKey());
//        params.addStringRequest(ParamsKey.PAYORDER_PWD, pwd);
//        params.addStringRequest(ParamsKey.PAYORDER_ORDERNO, orderNumber);
//        params.addStringRequest(ParamsKey.PAYORDER_MONEY,"0.01");
//        params.addStringRequest(ParamsKey.PAYORDER_PAYMENT, payMent);
//        params.addStringRequest(ParamsKey.PAYORDER_REMARK,"测试");
//        params.addStringRequest(ParamsKey.PAYORDER_SOURCE, "YC_APP");
//        String signJHB = encryption(getMyApplication().getUserKey(),
//                                    pwd,
//                                    orderNumber,
//                                    "0.01",
//                                    payMent,
//                                    "测试",
//                                    "YC_APP",
//                                    "SK@wohui.com-!@#123");
//        params.addStringRequest(ParamsKey.PAYORDER_SIGN,signJHB);
//        myOkHttpUtils.postRequest(Urls.WHKPAY, params, new Callback<JSONObject>() {
//            @Override
//            public JSONObject parseNetworkResponse(Response response) throws Exception {
//                String strJson = response.body().string();
//                LogUtils.e("我会卡支付返回json："+strJson);
//                JSONObject json = new JSONObject(strJson);
//                return json;
//            }
//
//            @Override
//            public void onError(Call call, Exception e) {
//                LogUtils.e("错误"+e.toString());
//                showToast(ctx,"网络错误："+e.toString());
//            }
//
//            @Override
//            public void onResponse(JSONObject response) {
//                String respMsg = "";
//                String respCode = "";
//                try {
//                    respMsg = response.getString("respMsg");
//                    respCode = response.getString("respCode");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if(respCode.equals("0000")){
//                    showToast(ctx, "支付成功");
//                    Intent intent = new Intent(ctx,OrderDetailsActivity.class);
//                    intent.putExtra("from","WXPayEntryActivity");
//                    ctx.startActivity(intent);
//                    PayOrderActivity.this.finish();
//                }else {
//                    showToast(ctx,"支付失败:"+respMsg);
//                }
//
//            }
//        });
//
//    }
    /**
     * 重载的可变参数加密算法
     *
     * @param strings
     * @return
     */
    private String encryption(String... strings) {
        String str = "";
        for (String string : strings) {
            str = str + string;
        }
        return MD5Encoder.encode(str);

    }
    //网络请求得到预付单ID
    private void getPrepayId() {
        LogUtils.e("getPrepayId");
        showDialog(ctx);
        payOrder.setClickable(false);
        params.clear();
        params.addStringRequest(ParamsKey.GetWXPryId_orderNumber,orderNumber);
        String strUrl = Urls.GetWXPryId;
        myOkHttpUtils.postRequest(strUrl, params, new WXPrepayIDCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e(e.toString());
                showToast(ctx, e.toString());
                payOrder.setClickable(true);
                dismissDialog();
            }
            @Override
            public void onResponse(WXPrepayIDBean response) {
                payOrder.setClickable(true);
                int state = response.getState();
                if(state==1){
                    DataEntity WXPrepayIdData = response.getData();
                    LogUtils.e(WXPrepayIdData.toString());
                    sendPayReqWX(WXPrepayIdData);//通过得到的微信预付单，进行微信支付
                    dismissDialog();
                }else if (state == 0) {
                    showToast(ctx, "数据为空" + response.getMsg());
                    LogUtils.e("state:"+state);
                    dismissDialog();
                } else {
                    showToast(ctx, "发生异常" + response.getMsg());
                    dismissDialog();
                    LogUtils.e("state:" + state);
                }
            }
        });
    }

    /**
     * 发送支付请求，打开微信支付界面
     */
    private void sendPayReqWX(DataEntity wXPrepayIdData){
        PayReq req = new PayReq();
        // 商户在微信开放平台申请的应用id
        req.appId = wXPrepayIdData.getAppid();
        // 商户id
        req.partnerId = wXPrepayIdData.getPartnerid();
        // 预支付订单
        req.prepayId = wXPrepayIdData.getPrepayid();
        //通过预付单生成的签名
        req.sign= wXPrepayIdData.getSign();
        // 商家根据文档填写的数据和签名 暂填写固定值Sign=WXPay
        req.packageValue = "Sign=WXPay";
        // 随机串，防重发
        req.nonceStr = wXPrepayIdData.getNoncestr();
        // 时间戳，防重发
        req.timeStamp = wXPrepayIdData.getTimestamp();
//        req.extData	= "app data";
        //发送支付请求
        boolean b = api.sendReq(req);
        LogUtils.e("b:"+b);
        if (b){

            //开启一个线程等延迟一秒关闭当前activity
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("ACTIVITY_FINISHI;;"+ACTIVITY_FINISHI);
                    handler.sendEmptyMessageDelayed(ACTIVITY_FINISHI,3000);
                }
            }).start();
        }
    }
    /**
     * 选择支付方式
     */
    private void selectPayMethod(String payMethod) {
        this.payMethod = payMethod;
        payOrder.setEnabled(true);
        switch (payMethod) {
            case "AL":
                payMethodXFJJ.setChecked(false);
                payMethodAL.setChecked(true);
                payMethodWX.setChecked(false);
                payMethodWH.setChecked(false);
                etPassword.setVisibility(View.GONE);
                payType = PAY_TYPE_AL;
                break;
            case "WH":
                payMethodXFJJ.setChecked(false);
                payMethodAL.setChecked(false);
                payMethodWX.setChecked(false);
                payMethodWH.setChecked(true);
                etPassword.setText("");
                etPassword.setVisibility(View.VISIBLE);
                payType = PAY_TYPE_WH;
                break;
            case "WX":
                payMethodXFJJ.setChecked(false);
                payMethodAL.setChecked(false);
                payMethodWX.setChecked(true);
                payMethodWH.setChecked(false);
                etPassword.setVisibility(View.GONE);
                payType = PAY_TYPE_WX;
                break;
            case "XFJJ":
                payMethodXFJJ.setChecked(true);
                payMethodAL.setChecked(false);
                payMethodWX.setChecked(false);
                payMethodWH.setChecked(false);
                etPassword.setVisibility(View.VISIBLE);
                payType = PAY_TYPE_XFJJ;
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (MyUtils.isHideInput(view, ev)) {
                MyUtils.HideSoftInput(view.getWindowToken(), ctx);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 生成随机数
     *
     * @return
     */
    private String genNonceStrWX() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }
    /**
     * 获取时间戳
     *
     * @return
     */
    private long genTimeStampWX() {
        return System.currentTimeMillis() / 1000;
    }
}
