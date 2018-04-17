package zz.wohui.wohui365.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.NormalBean;
import zz.wohui.wohui365.Bean.OrderDetailData;
import zz.wohui.wohui365.Bean.OrderListBean;
import zz.wohui.wohui365.activity.EvaluateOrderActivity;
import zz.wohui.wohui365.activity.LoginActivity;
import zz.wohui.wohui365.activity.OrderDetailsActivity;
import zz.wohui.wohui365.activity.PayOrderActivity;
import zz.wohui.wohui365.adapter.OrderListAdapter;
import zz.wohui.wohui365.callback.NormalCallBack;
import zz.wohui.wohui365.callback.OrderCallBack;
import zz.wohui.wohui365.listener.SwipeListViewOnScrollListener;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.ESLog;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.MainActivity;
import zz.wohui.wohui365.R;

/**
 * 说明：订单页面
 * 作者：陈杰宇
 * 时间： 2016/1/7 15:56
 * 版本：V1.0
 * 修改历史：
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        OrderListAdapter.MyItemBtnClickCallback {

    private RelativeLayout rlNoOrder;
    private TextView tvNoOrder;
    private RelativeLayout rlUserNoLogin;
    private Button btnLogin;
    private TextView tvTitleName;
    private RelativeLayout rlTitleGoBack;
    private PullToRefreshListView orderPullList;
    private ImageView ivMore;
    private LinearLayout llTitleMain;
    private SwipeRefreshLayout swipeRefLayout;
    private void assignViews(View v) {
        rlUserNoLogin = (RelativeLayout) v.findViewById(R.id.rlUserNoLogin);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);
        tvNoOrder = (TextView) v.findViewById(R.id.tvNoOrder);
        llTitleMain = (LinearLayout) v.findViewById(R.id.llTitleMain);
        swipeRefLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefLayout);
        ivMore = (ImageView) v.findViewById(R.id.ivTitleMore);
        rlNoOrder = (RelativeLayout) v.findViewById(R.id.rlNoOrder);
        tvTitleName = (TextView) v.findViewById(R.id.titleName);
        rlTitleGoBack = (RelativeLayout) v.findViewById(R.id.titleGoBack);
        orderPullList = (PullToRefreshListView) v.findViewById(R.id.orderPullList);
    }

    private Context ctx;
    private ListView listView;
    private OrderListAdapter orderListAdapter;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private final static int GET_ALL_ORDER = 1;
    private final static int GET_NO_EVALUTION_ORDER = 2;
    private final static int GET_REFUND_ORDER = 3;
    public final static int RESULT_FROM_COMMENT = 4;
    private int nowOrderType = GET_ALL_ORDER;
    private int nowPager = 1;
    private int totalPager;
    private int singlePagerNum = 5;
    private List<OrderDetailData> orderList;
    private PopupWindow popupWindow;
    private TextView tvAllOrder;
    private TextView tvNoEvaluateOrder;
    private TextView tvRefundOrder;
    private MainActivity mainActivity;
    private final static int ON_REFRESH_COMPLETE = 10001;

    @Override
    public View initView() {
        ctx = this.getContext();
        mainActivity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.order_tab_fragment_all, null);
        assignViews(v);
        init();
        return v;
    }

    /**
     * 初始化只有在第一次切换到这个页面的时候才加载
     */
    private void init() {
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        rlUserNoLogin.setVisibility(View.GONE);
        rlTitleGoBack.setVisibility(View.GONE);
        tvTitleName.setText("订单");
        ivMore.setVisibility(View.VISIBLE);
        MyUtils.initPullList(orderPullList, ctx);
        listView = orderPullList.getRefreshableView();
        swipeRefLayout.setColorSchemeResources(R.color.mainColor);
        initListener();
        getOrder(nowPager, GET_ALL_ORDER);
    }
    /**
     * 每次切换都会重新加载
     */
    @Override
    public void initEachData() {
        if (!isLogin()) {
            rlUserNoLogin.setVisibility(View.VISIBLE);
        } else {
            rlUserNoLogin.setVisibility(View.GONE);
        }
    }
    private void initListener() {
        orderPullList.setOnScrollListener(new SwipeListViewOnScrollListener(swipeRefLayout));
        ivMore.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        swipeRefLayout.setOnRefreshListener(this);
        orderPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (nowPager < totalPager) {
                    getOrder(++nowPager, nowOrderType);
                } else {
                    showToast(ctx, "没有更多内容");
                    handler.sendEmptyMessage(ON_REFRESH_COMPLETE);
                }
            }
        });
        orderPullList.setOnItemClickListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ON_REFRESH_COMPLETE:
                    orderPullList.onRefreshComplete();
                    break;
            }
        }
    };

    /**
     * 获取订单
     */
    private void getOrder(int page, int orderType) {
        showDialog(ctx);
        params.clear();
        String userKey = mainActivity.getMyApplication().getUserKey();
        params.addStringRequest(ParamsKey.userKey, userKey);
        params.addStringRequest(ParamsKey.order_Page, page + "");
        params.addStringRequest(ParamsKey.order_Rows, singlePagerNum + "");
        String strUrl = "";
        switch (orderType) {
            case GET_ALL_ORDER:
                nowOrderType = GET_ALL_ORDER;
                strUrl = Urls.GetOrderList;
                break;
            case GET_NO_EVALUTION_ORDER:
                nowOrderType = GET_NO_EVALUTION_ORDER;
                strUrl = Urls.GetOrderListForNoEvaluation;
                break;
            case GET_REFUND_ORDER:
                nowOrderType = GET_REFUND_ORDER;
                strUrl = Urls.GetOrderListForReturnGoods;
                break;
        }
        myOkHttpUtils.postRequest(strUrl, params, new OrderCallBack() {
            @Override

            public void onError(Call call, Exception e) {
                dismissDialog();
                showToast(ctx, MyUtils.networkError);
                rlNoOrder.setVisibility(View.VISIBLE);
                orderPullList.setVisibility(View.GONE);
                tvNoOrder.setText("暂无相关订单,或下拉刷新重试");
                handler.sendEmptyMessage(ON_REFRESH_COMPLETE);
            }

            @Override
            public void onResponse(OrderListBean response) {
                if (response.getState() == 1) {
                    fillOrderList(response);
                } else {
                    rlUserNoLogin.setVisibility(View.VISIBLE);
                    dismissDialog();
                }
            }
        });
    }

    /**
     * 填充订单列表
     *
     * @param response 订单的实体
     */
    private void fillOrderList(OrderListBean response) {
        if (response.getData().getDataCount() == 0) {
            rlNoOrder.setVisibility(View.VISIBLE);
            orderPullList.setVisibility(View.GONE);
            tvNoOrder.setText("暂无相关订单记录");
        } else {
            rlNoOrder.setVisibility(View.GONE);
            orderPullList.setVisibility(View.VISIBLE);
            if (orderList == null) {
                orderList = response.getData().getData();
            } else {
                if (totalPager == 0) {
                    orderList.clear();
                    orderList.addAll(response.getData().getData());
                } else {
                    orderList.addAll(response.getData().getData());
                }
            }
            int total = response.getData().getDataCount();
            totalPager = total % singlePagerNum == 0
                    ? total / singlePagerNum : total / singlePagerNum + 1;
            if (orderListAdapter == null) {
                orderListAdapter = new OrderListAdapter(orderList, ctx, this);
                listView.setAdapter(orderListAdapter);
            } else {
                orderListAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(ON_REFRESH_COMPLETE);
            }
        }
        dismissDialog();
        swipeRefLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleMore:
                showPopWind();
                break;
            case R.id.btnLogin:
                Intent intent = new Intent();
                intent.setClass(ctx, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.tvAllOrder:
                changeOrderType(GET_ALL_ORDER);
                break;
            case R.id.tvNoEvaluateOrder:
                changeOrderType(GET_NO_EVALUTION_ORDER);
                break;
            case R.id.tvRefundOrder:
                changeOrderType(GET_REFUND_ORDER);
                break;
        }
    }

    private void changeOrderType(int orderType) {
        totalPager = 0;
        nowPager = 1;
        getOrder(nowPager, orderType);
        popupWindow.dismiss();
    }

    /**
     * 显示弹出框PopWindow
     */
    private void showPopWind() {
        if (popupWindow == null) {
            View v = View.inflate(ctx, R.layout.pop_order_type, null);
            tvAllOrder = (TextView) v.findViewById(R.id.tvAllOrder);
            tvNoEvaluateOrder = (TextView) v.findViewById(R.id.tvNoEvaluateOrder);
            tvRefundOrder = (TextView) v.findViewById(R.id.tvRefundOrder);
            tvAllOrder.setOnClickListener(this);
            tvNoEvaluateOrder.setOnClickListener(this);
            tvRefundOrder.setOnClickListener(this);
            popupWindow = new PopupWindow(v, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        popupWindow.showAsDropDown(ivMore, 0, (llTitleMain.getHeight() - ivMore.getHeight()) / 2);
    }

    @Override
    public void onRefresh() {
        nowPager = 1;
        totalPager = 0;
        getOrder(nowPager, nowOrderType);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ESLog.e("" +
                position);
        int pst = position - 1;
        Intent intent = new Intent(ctx, OrderDetailsActivity.class);
        OrderDetailData bean = orderList.get(pst);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("OrderRowEntity", bean);
        intent.putExtra("from", "OrderFragment");
        ctx.startActivity(intent);
    }

    /**
     * activity再次显示的时候回调该方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_FROM_COMMENT:
                onRefresh();
                LogUtils.e("***" + resultCode);
        }
    }

    /**
     * 来自于列表内部按钮点击事件的回调
     *
     * @param v 受点击的控件
     */
    @Override
    public void itemBtnClick(View v) {
        int position = (int) v.getTag();
        OrderDetailData orderRowEntity = orderList.get(position);
        switch (v.getId()) {
            case R.id.btnEvaluate:

                Intent intent = new Intent(ctx, EvaluateOrderActivity.class);
                intent.putExtra("orderRowEntity", orderRowEntity);
                this.startActivityForResult(intent, 0);
                break;
            case R.id.btnRefund:
                if (orderRowEntity.getO_Status() == 20) {
                    cancelOrder(orderRowEntity);
                } else if (orderRowEntity.getO_Status() == 33) {
                    refundOrder(orderRowEntity);
                }else{
                    showToast(ctx,"订单状态错误:"+orderRowEntity.getO_Status());
                }


                break;
            case R.id.btnConfirm:
                int state = orderRowEntity.getO_Status();
                if (state == 10) {//代付款
                    String orderNumber = orderRowEntity.getO_GUID();//得到订单编号
                    pay(orderNumber);
                } else if (state == 32) {//确认收货
                    confirmGoods(orderRowEntity);
                }else {
                    showToast(ctx,"未知订单状态!");
                }
                break;
            case R.id.ivDelOrder:
                // TODO: 2016/3/17 删除订单
                delOrder(orderRowEntity);
                break;
        }
    }

    /**
     * 跳转到付款页面
     *
     * @param orderNumber 订单编号
     */
    private void pay(String orderNumber) {
        Intent intent = new Intent(ctx, PayOrderActivity.class);
        intent.putExtra("orderNumber", orderNumber);
        startActivity(intent);
    }

    /**
     * 申请退款
     *
     * @param orderRowEntity
     */
    private void refundOrder(final OrderDetailData orderRowEntity) {
        final String orderNumber = orderRowEntity.getO_GUID();
        final String[] items =
                new String[]{"未按约定时间接单", "未按照约定时间配货", "商品质量为题", "收到商品与描述不符", "收到商品破损"};

        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
        final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            int checkID = 10;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case -2:
                        break;
                    case -1:
                        refundRequest(orderNumber,checkID);
                        break;
                    case 0:
                        checkID = 10;
                        break;
                    case 1:
                        checkID = 20;
                        break;
                    case 2:
                        checkID = 30;
                        break;
                    case 3:
                        checkID = 40;
                        break;
                    case 4:
                        checkID = 50;
                        break;
                }
                LogUtils.e("第几条数据::"+which);
                LogUtils.e("getId():"+getId());
            }
        };
        adb.setSingleChoiceItems(items, 0, clickListener);
        adb.setTitle("申请退款");
//        adb.setMessage("您是否要进行申请退款?");
        adb.setCancelable(false);
        adb.setNegativeButton("取消", clickListener);
        adb.setPositiveButton("确定", clickListener).show();
    }

    /**
     * 申请退款
     * @param orderNumber
     * @param reason
     */
    private void refundRequest(String orderNumber, final int reason){
        showDialog(ctx);
        String strUrl = Urls.RefundRequest;
        params.clear();
        params.addStringRequest(ParamsKey.userKey, mainActivity.getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.RefundRequest_OrderNumber,orderNumber);
        params.addStringRequest(ParamsKey.RefundRequest_Reason,reason+"");
        myOkHttpUtils.postRequest(strUrl, params, new NormalCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, MyUtils.networkError);
                dismissDialog();
            }

            @Override
            public void onResponse(NormalBean response) {
                if(response.getState()==1){
                    Toast.makeText(ctx, "申请成功，等待商家确认", Toast.LENGTH_SHORT).show();
                    onRefresh();//确认成功刷新页面
                }else {
                    Toast.makeText(ctx, "申请失败:"+response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * 确认收货,通过Dialog弹出确定按钮，主要操作在确认监听
     *
     * @param orderRowEntity
     */
    private void confirmGoods(OrderDetailData orderRowEntity) {
        final String serialNumber = orderRowEntity.getO_GUID();

        AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
        adb.setTitle("确认收货");
        adb.setMessage("您是否已经收到所购买的商品？");
        adb.setCancelable(false);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                params.clear();
                params.addStringRequest(ParamsKey.userKey, mainActivity.getMyApplication().getUserKey());
                params.addStringRequest(ParamsKey.ConfimGoods_serialNumber, serialNumber);
                String strUrl = Urls.ConfimGoodsReceipt;
                myOkHttpUtils.postRequest(strUrl, params, new NormalCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.e("确认收货:" + e.toString());
                        Toast.makeText(ctx, "确认失败，请检查网络!" + e.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(NormalBean response) {
                        if (response.getState() == 1) {
                            Toast.makeText(ctx, "确认成功", Toast.LENGTH_SHORT).show();
                            onRefresh();//确认成功刷新页面
                        }
                    }
                });
            }
        });
        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();

    }

    /**
     * 删除订单
     *
     * @param orderRowEntity 删除的订单
     */
    private void delOrder(final OrderDetailData orderRowEntity) {
        MyUtils.showAlertDialog(ctx, "提示", "确认删除订单？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog(ctx);
                String strUrl = Urls.DelOrder;
                params.clear();
                params.addStringRequest(ParamsKey.userKey, mainActivity.getMyApplication().getUserKey());
                params.addStringRequest(ParamsKey.DelOrder_OrderNumber,
                        orderRowEntity.getO_GUID());
//                params.addStringRequest(ParamsKey.DelOrder_status, orderRowEntity.getO_Status() + "");
                //TODO 换下接口
                myOkHttpUtils.postRequest(strUrl, params, new NormalCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        showToast(ctx, MyUtils.networkError);
                        dismissDialog();
                    }
                    @Override
                    public void onResponse(NormalBean response) {
                        if (response.getState() != 1) {
                            showToast(ctx, "订单删除失败");
                            dismissDialog();
                        } else {
                            onRefresh();
                        }
                    }
                });
            }
        });

    }

    /**
     * 取消订单
     * @param orderRowEntity 取消的订单
     */
    private void cancelOrder(final OrderDetailData orderRowEntity) {
        MyUtils.showAlertDialog(ctx, "提示", "确认取消订单？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog(ctx);
                params.clear();
                params.addStringRequest(ParamsKey.userKey, mainActivity.getMyApplication().getUserKey());
                params.addStringRequest(ParamsKey.DelOrder_OrderNumber,
                        orderRowEntity.getO_GUID());
//                params.addStringRequest(ParamsKey.DelOrder_status, orderRowEntity.getO_Status() + "");
                //TODO 换下接口
                String strUrl = Urls.CancelOrder;
                myOkHttpUtils.postRequest(strUrl, params, new NormalCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        showToast(ctx, MyUtils.networkError);
                        dismissDialog();
                    }

                    @Override
                    public void onResponse(NormalBean response) {
                        if (response.getState() != 1) {
                            showToast(ctx, "订单取消失败:" + response.getMsg());
                            dismissDialog();
                        } else {
                            onRefresh();
                        }
                    }
                });
            }
        });

    }
}
