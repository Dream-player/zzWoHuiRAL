package zz.wohui.wohui365.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.NormalBean;
import zz.wohui.wohui365.Bean.OrderCommentBean;
import zz.wohui.wohui365.Bean.OrderDetailData;
import zz.wohui.wohui365.Bean.ShopDetailBean;
import zz.wohui.wohui365.callback.NormalCallBack;
import zz.wohui.wohui365.callback.OrderCommentCallBack;
import zz.wohui.wohui365.callback.ShopDetailCallBack;
import zz.wohui.wohui365.fragment.OrderFragment;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：订单评价页面
 * 作者：陈杰宇
 * 时间： 2016/2/16 15:13
 * 版本：V1.0
 * 修改历史：
 */
public class EvaluateOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvShopName;
    //    private ImageView ivGoodsImage;
//    private LinearLayout llGoodsName;
//    private TextView tvGoodsName;
//    private TextView tvGoodsPrice;
//    private TextView tvGoodsNum;
//    private TextView tvFactMoney;
    private RatingBar rbMark;
    private EditText tvFirstEstContent;
    private TextView tvAppendEstimate;
    private TextView etWriteEstimate;
    private TextView titleName;
    private RelativeLayout goBack;
    private Button btnSubmitEstimate;
    private LinearLayout firstComment;
    private TextView tvComment;

    private void assignViews() {
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleName = (TextView) findViewById(R.id.titleName);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
//        ivGoodsImage = (ImageView) findViewById(R.id.ivGoodsImage);
//        llGoodsName = (LinearLayout) findViewById(R.id.llGoodsName);
//        tvGoodsName = (TextView) findViewById(R.id.tvGoodsName);
//        tvGoodsPrice = (TextView) findViewById(R.id.tvGoodsPrice);
//        tvGoodsNum = (TextView) findViewById(R.id.tvGoodsNum);
//        tvFactMoney = (TextView) findViewById(R.id.tvFactMoney);
        rbMark = (RatingBar) findViewById(R.id.rbMark);
        tvFirstEstContent = (EditText) findViewById(R.id.tvFirstEstContent);
        tvAppendEstimate = (TextView) findViewById(R.id.tvAppendEstimate);
        etWriteEstimate = (TextView) findViewById(R.id.etWriteEstimate);
        btnSubmitEstimate = (Button) findViewById(R.id.btnSubmitEstimate);
        firstComment = (LinearLayout) findViewById(R.id.ll_first_comment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
    }

    private Activity ctx;
    private final static int SHOP_NAME_ERROR = 1100;//请求超市名字失败
    private final static int SHOP_NAME_SUCCESS = 1101;//成功请求到超市名字
    private final static int COMMENT_SUCCESS = 10003;//成功评论
    private final static int GET_ORDER_COMMETN_SUCCESS = 12222;//获取订单的评论内容成功
    private int statusComment;//评价状态
    private String numberSM;//超市编号
    private String serialNumber;//订单编号
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_evaluate);
        assignViews();
        titleName.setText("订单评价");
        ctx = this;
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        //根据intent传递过来的数据初始化内容
        initView();
        btnSubmitEstimate.setOnClickListener(this);//提交评价按钮添加监听
        goBack.setOnClickListener(this);//返回按钮添加监听

    }

    /**
     * 根据intent传递过来的数据初始化内容
     */
    private void initView() {
        Intent intent = getIntent();
        //得到传入的订单类
        OrderDetailData orderRowEntity
                = (OrderDetailData)
                intent.getSerializableExtra("orderRowEntity");
        //通过statusComment订单评价状态，来展示不同的布局
        statusComment = orderRowEntity.getO_CommentStatus();
        //得到订单编号
        serialNumber = orderRowEntity.getO_GUID();
        if (statusComment == 10) {
            //商家回复消失
            tvAppendEstimate.setVisibility(View.GONE);
            etWriteEstimate.setVisibility(View.GONE);
            firstComment.setVisibility(View.VISIBLE);
            rbMark.setIsIndicator(false);
            tvComment.setText("订单评价");
        } else if (statusComment == 20) {
            tvComment.setText("我的评价");
            rbMark.setIsIndicator(true);
            //把edittext设为不可编辑状态
            tvFirstEstContent.setEnabled(false);
            tvFirstEstContent.setFocusable(false);
            //确认按钮消失
            btnSubmitEstimate.setVisibility(View.GONE);
            //TODO 获得评价数据
            getOrderComment(serialNumber);
        }
        numberSM = orderRowEntity.getO_SMGUID();
        getShopName(numberSM);
        //得到订单的物品总数和物品名字
//        int count = 0;
//        int price = 0;
//        StringBuilder stringBuilder=new StringBuilder();
//        for (OrderDetailData.DetailEntity entity:orderRowEntity.getDetail()){
//            int i= entity.getOD_BuyNum();
//            count+=i;
//            price+= entity.getOD_TotalPrice()*i;
//            stringBuilder.append(entity.getCS_Name()+"*"+entity.getOD_BuyNum());
//        }

//        //进行网络请求，通过超市编号得到超市名称

//        //控件填充数据
//        tvGoodsPrice.setText("¥"+price);
//        tvGoodsNum.setText("共" + count + "件商品");
//        tvGoodsName.setText(stringBuilder.toString());
//        tvFactMoney.setText("实付：¥" + orderRowEntity.getO_TotalPrice());


    }

    private void getOrderComment(String orderNo) {
        showDialog(ctx);
        String strUrl = Urls.GetOrderComment;
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.GetOrderComment_orderNo, orderNo);
        myOkHttpUtils.postRequest(strUrl, params, new OrderCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e("获取订单评论内容错误提示:" + e.toString());
                showToast(ctx, "网络请求失败");
                dismissDialog();
            }

            @Override
            public void onResponse(OrderCommentBean response) {
                dismissDialog();
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.what = GET_ORDER_COMMETN_SUCCESS;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    /**
     * 进行网络请求，通过超市编号得到超市名称，
     *
     * @param numberSM 超市编号
     * @return 超市名称
     */
    private void getShopName(String numberSM) {
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.ShopSerialNumber, numberSM);
        String strUrl = Urls.GetShopDetail;


        myOkHttpUtils.postRequest(strUrl, params, new ShopDetailCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                handler.sendEmptyMessage(SHOP_NAME_ERROR);
                LogUtils.e("获取超市名称失败" + e.toString());
            }

            @Override
            public void onResponse(ShopDetailBean response) {
                if (response.getState() == 1) {
                    String str = response.getData().getS_Name();
                    Message msg = new Message();
                    msg.what = SHOP_NAME_SUCCESS;
                    msg.obj = str;
                    handler.sendMessage(msg);
                }

            }

        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOP_NAME_ERROR:
                    tvShopName.setText("超市");

                    break;
                case SHOP_NAME_SUCCESS:
                    tvShopName.setText((String) msg.obj);

                    break;
                case COMMENT_SUCCESS:
                    ctx.setResult(OrderFragment.RESULT_FROM_COMMENT);
                    ctx.finish();
                    break;
                case GET_ORDER_COMMETN_SUCCESS:
                    OrderCommentBean.DataEntity data = ((OrderCommentBean) msg.obj).getData();
                    rbMark.setRating(data.getOC_Score());
                    tvFirstEstContent.setText(data.getOC_CommentMsg());
                    if (!data.getOC_ReplyMsg().equals("")) {
                        tvAppendEstimate.setVisibility(View.VISIBLE);
                        etWriteEstimate.setVisibility(View.VISIBLE);
                        etWriteEstimate.setText(data.getOC_ReplyMsg());
                    }

                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmitEstimate:
                submitEstimate();
                break;
            case R.id.titleGoBack:
                EvaluateOrderActivity.this.onBackPressed();
                finish();
                break;
        }
    }

    /**
     * 提交评价
     */
    private void submitEstimate() {
        //  联网提交评价 完成以后跳转回订单页面
        //根据订单评价状态StatusComment来判断是进行首次评价或者追加评价
        //得到评价星级，不能不评
        int rating = Math.round(rbMark.getRating());
        if (rating == 0) {
            Toast.makeText(ctx, "评价星级不能为0", Toast.LENGTH_SHORT).show();
            dismissDialog();
            return;
        }
        //得到评价具体内容，切评价不能为空
        String estimate = tvFirstEstContent.getText().toString().trim();
        if (estimate.equals("")) {
            Toast.makeText(ctx, "请填写评价内容", Toast.LENGTH_SHORT).show();
            return;
        }
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.OrderCommentFirst_NumberSM, numberSM);
        params.addStringRequest(ParamsKey.OrderCommentFirst_NumberOrder, serialNumber);
        params.addStringRequest(ParamsKey.OrderCommentFirst_Score, rating + "");
        params.addStringRequest(ParamsKey.OrderCommentFirst_Content, estimate);
        String strURL = Urls.AddOrderCommentFirst;
        myOkHttpUtils.postRequest(strURL, params, new NormalCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e(e + "******" + e.toString());
                Toast.makeText(ctx, "评论失败，请检查网络", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(NormalBean response) {
                if (response.getState() == 1) {
                    LogUtils.e(response.toString() + "**********************");
                    Toast.makeText(ctx, "评论成功", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(COMMENT_SUCCESS);
                } else {
                    showToast(ctx, "错误信息"+response.getMsg());
                }

            }
        });


    }
}
