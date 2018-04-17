package zz.wohui.wohui365.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import zz.wohui.wohui365.Bean.AddressBean;
import zz.wohui.wohui365.Bean.ShopCartListBean;
import zz.wohui.wohui365.Bean.ShopCartListBean.DataEntity;
import zz.wohui.wohui365.adapter.ConfirmOrderAdapter;
import zz.wohui.wohui365.callback.AddressCallBack;
import zz.wohui.wohui365.callback.ShopCartListCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.utils.ShopCartDatabaseHelper;
import zz.wohui.wohui365.R;

/**
 * 说明：确认订单页面
 * 作者：陈杰宇
 * 时间： 2016/3/1 10:55
 * 版本：V1.0
 * 修改历史：
 */
public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    private ListView lvGoods;
    private LinearLayout llCheckAddress;
    private TextView tvDeliverStatus;
    private LinearLayout llDeliverStatus;
    private LinearLayout llOrderRemark;
    private ImageView ivPayOnline;
    private LinearLayout llPayOnLine;
    private ImageView ivPayOnFace;
    private LinearLayout llPayOnFace;
    private TextView tvTotalPrice;
    private TextView tvConfirmOrder;
    private RelativeLayout goBack;
    private TextView titleName;
    private LinearLayout llAddressItemMain;
    private TextView tvRecieverName;
    private TextView tvRecieverPhone;
    private TextView tvRecieverAddress;
    private ImageView ivAddressEdit;
    private EditText etMessageInput;
    private Context ctx;
    private ConfirmOrderAdapter orderAdapter;
    public static final int REQUEST_ADDRESS = 10;
    private String adressNumber = "";//代表的地址标号
    private String otherMsg = "亲,请尽快配送!";//订单添加的额外信息
    private Intent intent;
    private double totalPrice;
    private List<DataEntity> goodsList;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private String shopSerialNo;
    private int deliverStatus = 20;//收货方式，默认送货上门
    private static final int GETCARTLIST_OK = 222;
    private ShopCartDatabaseHelper myDBHelper;
    private double freight;
    private String userCard;
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        ctx = this;
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();

        initView();
        initData();
        //网络请求，获得当前服务器中本超市下的购物车列表
        getCartListBean();
        getUserAddress();//网络请求，获得用户的收货地址列表
    }

    /**
     * 网络请求服务器中购物车列表
     */
    private void getCartListBean() {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.GetCart_SMNo,shopSerialNo);
        String strUrl = Urls.ShopCartList;
        myOkHttpUtils.postRequest(strUrl, params, new ShopCartListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, "网络出错");
                dismissDialog();
            }

            @Override
            public void onResponse(ShopCartListBean response) {
                int state = response.getState();
                if (state == 1) {
                    showToast(ctx, "处理成功" + response.getMsg());
                    Message msg = new Message();
                    msg.what = GETCARTLIST_OK;
                    msg.obj = response;
                    handler.sendMessage(msg);
                } else if (state == 0) {
                    showToast(ctx, "数据为空" + response.getMsg());
                    dismissDialog();
                } else if (state == -1) {
                    showToast(ctx, "发生异常" + response.getMsg());
                    dismissDialog();
                }

            }
        });
    }

    private static final int ConfirmOrder_OK = 11122;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETCARTLIST_OK:
                    ShopCartListBean bean = (ShopCartListBean) msg.obj;
                    List<DataEntity> list = bean.getData();
                    goodsList.clear();
                    goodsList.addAll(list);
                    orderAdapter.notifyDataSetChanged();
                    totalPrice = getTotalPrice(goodsList)+freight;
                    tvTotalPrice.setText("¥" + totalPrice);
                    dismissDialog();
                    break;
                case ConfirmOrder_OK:
                    String orderNumber = (String) msg.obj;
                    myDBHelper.clearAllGoods();
                    Intent intent = new Intent(ctx, PayOrderActivity.class);
                    intent.putExtra("orderNumber", orderNumber);
                    startActivity(intent);
                    finish();
                    break;
                case AddressRequest_OK:
                    AddressBean.DataEntity defAdress = (AddressBean.DataEntity) msg.obj;
                    showAddress(defAdress);
                    break;
            }
        }
    };

    /**
     * 初始化数据
     */
    private void initData() {
        intent = getIntent();
        freight = intent.getDoubleExtra("freight",0);
        shopSerialNo = intent.getStringExtra("shopSerialNo");
        myDBHelper = getMyApplication().getMyDBHelper(shopSerialNo);
//        goodsList = myDBHelper.getAllGoods();
        goodsList = new ArrayList<>();
        totalPrice = myDBHelper.getTotalPrice();
        tvTotalPrice.setText("¥" + totalPrice);
//        LogUtils.e("购物车商品条目总数" + goodsList.size());
        orderAdapter = new ConfirmOrderAdapter(goodsList, ctx);
        lvGoods.setAdapter(orderAdapter);
        SharedPreferences sp = getSharedPreferences(MyKey.userConfig, MODE_PRIVATE);
        userCard = sp.getString("cardNumber","");
    }

    private double getTotalPrice(List<DataEntity> datas) {
        double totalPrice = 0;
        for (DataEntity data : datas) {
            totalPrice = MyUtils.add(totalPrice, data.getO_TotalPrice());
        }
        return totalPrice;
    }

    private void initView() {
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);

        titleName = (TextView) findViewById(R.id.titleName);
        titleName.setText("确认订单");
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        tvConfirmOrder = (TextView) findViewById(R.id.tvConfirmOrder);
        lvGoods = (ListView) findViewById(R.id.lvGoods);
        View vHeader = View.inflate(ctx, R.layout.confirm_order_list_header, null);
        getHeaderView(vHeader);
        lvGoods.addHeaderView(vHeader);
        llAddressItemMain.setVisibility(View.GONE);
        ivAddressEdit.setVisibility(View.GONE);
        ivPayOnFace.setEnabled(false);
        ivPayOnline.setEnabled(true);
        initListener();
    }


    /**
     * 获取头文件中的布局实例
     *
     * @param vHeader
     */
    private void getHeaderView(View vHeader) {
        llCheckAddress = (LinearLayout) vHeader.findViewById(R.id.llCheckAddress);
        tvDeliverStatus = (TextView) vHeader.findViewById(R.id.tvDeliverStatus);
        llDeliverStatus = (LinearLayout) vHeader.findViewById(R.id.llDeliverStatus);
        llOrderRemark = (LinearLayout) vHeader.findViewById(R.id.llOrderRemark);
        ivPayOnline = (ImageView) vHeader.findViewById(R.id.ivPayOnline);
        llPayOnLine = (LinearLayout) vHeader.findViewById(R.id.llPayOnLine);
        ivPayOnFace = (ImageView) vHeader.findViewById(R.id.ivPayOnFace);
        llPayOnFace = (LinearLayout) vHeader.findViewById(R.id.llPayOnFace);
        llAddressItemMain = (LinearLayout) vHeader.findViewById(R.id.llAddressItemMain);
        tvRecieverName = (TextView) vHeader.findViewById(R.id.tvRecieverName);
        tvRecieverPhone = (TextView) vHeader.findViewById(R.id.tvRecieverPhone);
        tvRecieverAddress = (TextView) vHeader.findViewById(R.id.tvRecieverAddress);
        ivAddressEdit = (ImageView) vHeader.findViewById(R.id.ivAddressEdit);
        etMessageInput = (EditText) vHeader.findViewById(R.id.et_message_input);
    }

    /**
     * 监听数据
     */
    private void initListener() {
        llAddressItemMain.setOnClickListener(this);
        goBack.setOnClickListener(this);
        llCheckAddress.setOnClickListener(this);
        llDeliverStatus.setOnClickListener(this);
        llOrderRemark.setOnClickListener(this);
        llPayOnLine.setOnClickListener(this);
        llPayOnFace.setOnClickListener(this);
        tvConfirmOrder.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                this.onBackPressed();
                break;
            case R.id.llCheckAddress:
                checkAddress();
                break;
            case R.id.llAddressItemMain:
                checkAddress();
                break;
            case R.id.llDeliverStatus:
                //配送方式
                showToast(this,"暂时只支持送货上门");
                break;
            case R.id.llOrderRemark:
                // 2016/3/1 订单备注
                getMessageDialog();
                break;
            case R.id.llPayOnLine:
                // 016/3/1 在线支付
                setPayStatus(true);
                break;
            case R.id.llPayOnFace:
                //  2016/3/1 货到付款
                showToast(ctx, "仅支持在线支付");
//                setPayStatus(false);
                break;
            case R.id.tvConfirmOrder:
                // 2016/3/1 提交订单
                if (adressNumber.equals("")) {
                    showToast(ctx, "请选择收货地址");
                    return;
                }
                MyUtils.showAlertDialog(ctx, "提示", "确定提交订单？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if ("".equals(userCard)){
                            showToast(ctx,"发生错误,请重新登录!");
                        }else {
                            confirmCart();//提交订单
                        }

                    }
                });
                break;
        }
    }

    /**
     * 提交购物车，生成订单
     */
    private void confirmCart() {
        String otherMsg = etMessageInput.getText().toString().trim();
        if (otherMsg.equals("")) {
            otherMsg = "亲,请尽快配送~";
        }
        tvConfirmOrder.setClickable(false);
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.CreatOrder_AddrNo, adressNumber);
        params.addStringRequest(ParamsKey.CreatOrder_ReceiptMethod, deliverStatus + "");
        params.addStringRequest(ParamsKey.CreatOrder_Remark, otherMsg);
        params.addStringRequest(ParamsKey.CreatOrder_SMNo, shopSerialNo);
        params.addStringRequest(ParamsKey.CreatOrder_YWCardNumber,userCard);
        String strUrl = Urls.CreatOrder;
        myOkHttpUtils.postRequest(strUrl, params, new Callback<JSONObject>() {
            @Override
            public JSONObject parseNetworkResponse(Response response) throws Exception {
                String str = response.body().string();
//                str.replace("\\","*");
                LogUtils.e("确认下单返回结果：" + str);
                JSONObject json = new JSONObject(str);
                return json;
            }

            @Override
            public void onError(Call call, Exception e) {
                tvConfirmOrder.setClickable(true);
                showToast(ctx, "网络出错,请重新购买!");
                dismissDialog();
            }
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                tvConfirmOrder.setClickable(true);
                try {
                    if (response.getInt("State") == 1) {
                        //生成订单成功之后，进行清空该超市下的本地购物车数据库
                        Message msg = new Message();
                        msg.what = ConfirmOrder_OK;
                        String str =  response.getString("Data");
                        if (str!=null){
                            msg.obj =str;
                            handler.sendMessageDelayed(msg, 200);
                        }else {
                            showToast(ctx, "下单失败" + response.getString("Msg"));
                        }
                    } else {
                        showToast(ctx, "下单失败:" + response.getString("Msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.e("解析出错" + e.toString());
                }

            }
        });
    }
    private static final int AddressRequest_OK = 4956;
    private static final int UserAddressRequest_Null = 7456;

    /**
     * 获取用户地址信息
     */
    private void getUserAddress() {
//        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        myOkHttpUtils.postRequest(Urls.GetConsigneeList, params, new AddressCallBack() {
            @Override
            public void onError(Call call, Exception e) {
//                dismissDialog();
                Toast.makeText(ctx, "获取地址失败请检查您的网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(AddressBean response) {
//                dismissDialog();
                if (response.getState() == 1) {
                    for (AddressBean.DataEntity addressData : response.getData()) {
                        if (addressData.isDA_IsDefault()) {//把默认的收货地址实体类传给主线程
                            Message msg = new Message();
                            msg.what = AddressRequest_OK;
                            msg.obj = addressData;
                            handler.sendMessage(msg);
                        }
                    }

                }

            }
        });
    }

    /**
     * 提交订单
     */
    private void confirmOrder() {
        Intent intent = new Intent(ctx, PayOrderActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 选择支付方式
     *
     * @param isOnLine 是否在线支付
     */
    private void setPayStatus(boolean isOnLine) {
        // TODO: 2016/3/2 设置支付方式
        if (isOnLine) {
            ivPayOnline.setEnabled(true);
            ivPayOnFace.setEnabled(false);
        } else {
            ivPayOnline.setEnabled(false);
            ivPayOnFace.setEnabled(true);
        }
    }

    /**
     * 显示配送方式dialog
     */
    private void showDeliverStatus() {
        new AlertDialog.Builder(this).setTitle("选择配送方式")
                .setSingleChoiceItems(new String[]{"送货上门", "自提"}, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    tvDeliverStatus.setText("送货上门");
                                    deliverStatus = 20;
                                } else if (which == 1) {
                                    tvDeliverStatus.setText("自提");
                                    deliverStatus = 10;
                                }
                                dialog.dismiss();
                                //设置配送方式信息
                            }
                        }).setNegativeButton("取消", null).show();
    }

    /**
     * 输入附加信息的dialog
     */
    private void getMessageDialog() {
        if (etMessageInput.getVisibility() == View.VISIBLE) {
            etMessageInput.setVisibility(View.GONE);
        } else if (etMessageInput.getVisibility() == View.GONE) {
            etMessageInput.setVisibility(View.VISIBLE);
        }
//        final EditText et = new EditText(ConfirmOrderActivity.this);
//        et.setHint(otherMsg);
//        et.setBackground(getResources().getDrawable(R.drawable.order_first_evaluate_bg));
//        et.setBackgroundResource(R.drawable.order_first_evaluate_bg);
////        et.setBackgroundResource(R.drawable.order_first_evaluate_bg);
////        et.setBackground(getDrawable(R.drawable.order_first_evaluate_bg));
//        et.setMinLines(3);
//        et.setPadding(20,20,20,20);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setView(et);
//        dialog.setTitle("附加信息");
//        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String str = et.getText().toString().trim();
//                if (str != null) {
//                    ConfirmOrderActivity.this.otherMsg = str;
//                }
//            }
//        });
//        dialog.setNegativeButton("取消", null);
//        dialog.show();
    }

    /**
     * 选择收货地址
     */
    private void checkAddress() {
        Intent intent = new Intent(ctx, AddressManageActivity.class);
        intent.putExtra("isFromOrder", true);
        startActivityForResult(intent, REQUEST_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADDRESS:
                if (resultCode == RESULT_OK) {
                    AddressBean.DataEntity bean = (AddressBean.DataEntity) data.getSerializableExtra("data");
                    showAddress(bean);
                }
                break;
        }
    }

    private void showAddress(AddressBean.DataEntity bean) {

        llAddressItemMain.setVisibility(View.VISIBLE);
        llCheckAddress.setVisibility(View.GONE);

        llAddressItemMain.setVisibility(View.VISIBLE);
        llCheckAddress.setVisibility(View.GONE);

        tvRecieverName.setText(bean.getDA_RealName());
        tvRecieverPhone.setText(bean.getDA_Phone());
        tvRecieverAddress.setText(bean.getDA_Towns()+bean.getDA_Address());
        adressNumber = bean.getDA_GUID();
    }


}
