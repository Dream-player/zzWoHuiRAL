package whcs.wohui.zz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.callback.Callback;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import whcs.wohui.zz.Bean.AddCartBean;
import whcs.wohui.zz.Bean.DBgoodsData;
import whcs.wohui.zz.Bean.GoodsListBean;

import whcs.wohui.zz.Bean.GoodsListBean.DataEntity.GoodsEntity;
import whcs.wohui.zz.adapter.GoodsListAdapter;
import whcs.wohui.zz.adapter.ShopCartAdapter;
import whcs.wohui.zz.callback.AddCartCallBack;
import whcs.wohui.zz.callback.GoodsListCallBack;
import whcs.wohui.zz.url.ParamsKey;
import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.utils.MyRequestParams;
import whcs.wohui.zz.utils.MyUtils;
import whcs.wohui.zz.utils.ShopCartDatabaseHelper;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：商店内搜索页面
 * 作者：陈杰宇
 * 时间： 2016/2/29 14:07
 * 版本：V1.0
 * 修改历史：
 */
public class ShopSearchActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout rlNoData;
    private TextView tvNoData;
    private PullToRefreshListView lvGoods;
    private RelativeLayout shopCartMain;
    private ImageView image;
    private TextView shopCartNum;
    private TextView totalPrice;
    private TextView howMoneyToDelivery;
    private TextView goTOCheckOut;
    private RelativeLayout titleGoBack;
    private EditText titleEdit;
    private TextView titleName;
    private RelativeLayout titleDoSearch;
    private View noData;

    private void assignViews() {
        noData = findViewById(R.id.noData);
        lvGoods = (PullToRefreshListView) findViewById(R.id.lvGoods);
        shopCartMain = (RelativeLayout) findViewById(R.id.shopCartMain);
        image = (ImageView) findViewById(R.id.image);
        shopCartNum = (TextView) findViewById(R.id.shopCartNum);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        howMoneyToDelivery = (TextView) findViewById(R.id.howMoneyToDelivery);
        goTOCheckOut = (TextView) findViewById(R.id.goTOCheckOut);
        titleGoBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        titleName = (TextView) findViewById(R.id.titleName);
        titleDoSearch = (RelativeLayout) findViewById(R.id.titleDoSearch);
    }
    private Context ctx;
    private List<GoodsEntity> goodsList;//网络请求的商品
    private List<Integer> goodsNumList;//当前展示列表订购的商品数量
    private double price =0;//购买总钱数
    private GoodsListAdapter myGoodslistAdapter;
    private ShopCartDatabaseHelper myDBHelper;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private String shopSerialNo;
    private double minMoney;
    private double freight;
    private int pageIndex;
    private String goodsName;
    private List<DBgoodsData> dBgoodsDataList;//数据表中总物品
    private static final int GET_GL_SUCCESS = 1150;//得到商品成功
    private static final int GET_GL_DEFEAT = 1151;//得到商品失败
    private static final int ConfirmCart_SUCCESS = 7786;//确认购物车成功

    private int totalCount;//数据表中物品总数量
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_search);
        assignViews();
        ctx = this;
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        // 根据获取到的信息填充标题等，根据超市标号获取数据库工具类
        Intent intent = getIntent();
        shopSerialNo = intent.getStringExtra("shopSerialNo");
        minMoney = intent.getDoubleExtra("MinMoney",20);
        freight = intent.getDoubleExtra("freight",0);
        myDBHelper = getMyApplication().getMyDBHelper(shopSerialNo);
        //
        dBgoodsDataList = new ArrayList<>();
        initCart();
        initView();
        initGoodsList();
        initListener();
    }
    /**
     * 初始化list，配置适配器
     */
    private void initGoodsList(){
        MyUtils.initPullList(lvGoods, ctx);
        goodsList = new ArrayList<>();
        goodsNumList = new ArrayList<>();
        myGoodslistAdapter = new GoodsListAdapter(goodsList,ctx,myDBHelper);
        //设置适配器参数
        myGoodslistAdapter.setmActivity(this);
        myGoodslistAdapter.setShopCart(shopCartNum);
        myGoodslistAdapter.setGoodsNum(goodsNumList);
        myGoodslistAdapter.setOnShopCartGoodsChangeListener(new GoodsListAdapter.OnShopCartGoodsChangeListener() {
            @Override
            public void onNumAdd(int position) {
                totalCount++;
                GoodsEntity entity = goodsList.get(position);
                myDBHelper.goodsAdd(entity);
                price = MyUtils.add(price, entity.getSMC_UnitPrice());
                setCartView(price);
            }

            @Override
            public void onNumDecrease(int position) {
                totalCount--;
                GoodsEntity entity = goodsList.get(position);
                myDBHelper.goodsDecrease(entity);
                price = MyUtils.sub(price, entity.getSMC_UnitPrice());
                setCartView(price);
            }
        });
        lvGoods.setAdapter(myGoodslistAdapter);
        lvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //  进行搜索
                goodsName = titleEdit.getText().toString().trim();//得到当前搜索商品名称
                getGoods(shopSerialNo, goodsName, pageIndex);
            }
        });
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        myAddTextChangeListener = new MyAddTextChangeListener();
        titleEdit.addTextChangedListener(myAddTextChangeListener);
        titleGoBack.setOnClickListener(this);
        shopCartMain.setOnClickListener(this);
        goTOCheckOut.setOnClickListener(this);
        titleDoSearch.setOnClickListener(this);
        howMoneyToDelivery.setOnClickListener(this);

    }
    //初始化购物车
    private void initCart() {
        price = 0;
        totalCount = 0;
        dBgoodsDataList.clear();
        dBgoodsDataList.addAll(myDBHelper.getAllGoods());
        for (DBgoodsData data:dBgoodsDataList){
            totalCount+=data.getOrderCount();//初始化购物车商品总数
            price = MyUtils.add(price, MyUtils.mul(data.getOrderCount(), data.getPriceUnit()));//初始化购物车商品总价
        }
        setCartView(price);//给底部购物车界面设置总价钱
        changeShopCart(totalCount);// 给底部购物车界面设置总数量
    }
    //改变购物车物品总数
    private void changeShopCart(int totalCount) {
        if (totalCount > 0) {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(totalCount + "");
        } else {
            shopCartNum.setVisibility(View.GONE);
        }
    }
    private void initView(){
        titleName.setVisibility(View.GONE);
        titleDoSearch.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setHint("请输入您要搜索的商品");
        tvNoData = (TextView) noData.findViewById(R.id.tvNoData);
        tvNoData.setText("没有搜索到商品\n换个关键词试试");
        noData.setVisibility(View.GONE);
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


    private MyAddTextChangeListener myAddTextChangeListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                this.onBackPressed();
                finish();
                break;
            case R.id.titleDoSearch://搜索商品按钮
                goodsName = titleEdit.getText().toString().trim();//得到当前搜索商品名称
                pageIndex = 1;//初始化当前页数
                getGoods(shopSerialNo,goodsName,pageIndex);
                break;
            case R.id.goTOCheckOut://去结算按钮
                //去结算的时候，先进行清空后台购物车的操作
                //清空之后，再进行向后台添加当前购物车数据库中的商品
                checkOut(shopSerialNo,0);//向该超市添加新的购物车
                break;
            case R.id.shopCartMain://显示购物车,弹窗
                showShopCart();
                break;
            case R.id.howMoneyToDelivery:
                int goodsNumber = myDBHelper.getAllGoodsNumber();
                if (goodsNumber<1){
                    showToast(ctx,"您还没有选择商品!");
                    return;
                }
                MyUtils.showAlertDialog(ctx, "提示",
                        Html.fromHtml("订单低于商家起送价,将会会收取额外的配送费"+"<font color='red'>￥"+freight+"</font>"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkOut(shopSerialNo,freight);
                            }
                        });
                break;
        }
    }
    /**
     * 清空后台在某个超市下的购物车
     * @param shopSerialNo 超市编号
     */
    private void clearShopCart(final String shopSerialNo){
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.ClearCart_SMNo,shopSerialNo);
        String strUrl = Urls.ClearCart;
        myOkHttpUtils.postRequest(strUrl, params, new Callback<JSONObject>() {
            @Override
            public JSONObject parseNetworkResponse(Response response) throws Exception {
                String str = response.body().string();
                LogUtils.e("清空购物车json数据："+str);
                JSONObject json = new JSONObject(str);
                return json;
            }

            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, "网络出错：" + e.toString());
                dismissDialog();
            }

            @Override
            public void onResponse(JSONObject response) {
                //如果后台有购物车则成功，没有则操作失败
                checkOut(shopSerialNo,freight);//向该超市添加新的购物车
            }
        });
    }
    /**
     * 去结算
     */
    private void checkOut(final String shopSerialNo, final double fright) {
        //确认购物车接口 CartList 参数
        goTOCheckOut.setClickable(false);
        List<DBgoodsData> goodsList = myDBHelper.getAllGoods();
        String CartList = getStrCartList(goodsList);
//        String CartList = "[{\"SMNo\":\"4474cab1-354d-4426-9c1e-9b1f88fb2c47\",\"CommNo\":\"22fbb82d-0ee2-4cdf-935e-bbf8f52e3b05\\\",\\\"BuyNum\\\":\\\"8\\\"}, {\\\"SMNo\\\":\\\"4474cab1-354d-4426-9c1e-9b1f88fb2c47\\\",\\\"CommNo\\\":\\\"0db98bc5-705d-49a8-bf61-5f8f30866b55\\\",\\\"BuyNum\\\":\\\"2\\\"}]";
        params.clear();
        params.addStringRequest(ParamsKey.AddCart_Json, CartList);
        params.addStringRequest(ParamsKey.userKey, this.getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.AddCart_SMNo,shopSerialNo);
        String strUrl = Urls.ConfirmCart;
        myOkHttpUtils.postRequest(strUrl, params, new AddCartCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                showToast(ctx, "网络出错，请检查网络");
                LogUtils.e("网络出错，请检查网络" + e.toString());
                dismissDialog();
                goTOCheckOut.setClickable(true);
//            }
            }

            @Override
            public void onResponse(AddCartBean response) {
                if(response.getData().isIsSuccess()){
                    showToast(ctx,response.getData().getMessage());
                    //此处发送延迟消息，添加购物车之后，
                    //下个跳转页面需要向后台请求购物车列表数据
                    //这个延迟时间是给后台处理的时间
                    Message msg = new Message();
                    msg.obj = fright;
                    msg.what = ConfirmCart_SUCCESS;
                    handler.sendMessageDelayed(msg,500);
                }else {
                    showToast(ctx, "添加失败:" + response.getData().getMessage());
                    dismissDialog();
                    goTOCheckOut.setClickable(true);
                }
            }
        });
    }

    private String[] testGoods = {"玉兰油", "波尔多", "拉菲", "德国晚秋清甜", "法国贵腐酒", "蓝艳冰", "美国加州乐事顺柔白葡萄酒", "进口红葡萄酒"};

    /**
     * 网络请求，获取商品
     */
    private void getGoods(String shopSerialNo,String goodsName,int pageIndex) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.GoodsSearch_searchName, goodsName);
        params.addStringRequest(ParamsKey.GetGoods_goodsOrder, "1");
        params.addStringRequest(ParamsKey.GetGoods_shopSerialNo,shopSerialNo);
        params.addStringRequest(ParamsKey.GetGoods_pageIndex,""+pageIndex);
        params.addStringRequest(ParamsKey.GetGoods_pageSize, "10");
        String strUrl = Urls.GoodsSearch;
        myOkHttpUtils.postRequest(strUrl, params, new GoodsListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e("搜索商品错误:"+e.toString());

                handler.sendEmptyMessage(GET_GL_DEFEAT);

            }
            @Override
            public void onResponse(GoodsListBean response) {
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = GET_GL_SUCCESS;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_GL_DEFEAT:
                    Toast.makeText(ctx, "网络请求失败", Toast.LENGTH_SHORT).show();
                    dismissDialog();
                    break;
                case GET_GL_SUCCESS:
                    GoodsListBean data = (GoodsListBean) msg.obj;//获取网络请求商品列表
                    if (pageIndex==1){
                        goodsList.clear();
                        goodsNumList.clear();
                        //如果没有数据则展示
                        if (data.getData().getData().size()==0){
                            noData.setVisibility(View.VISIBLE);
                            lvGoods.setVisibility(View.GONE);
                        }else {
                            noData.setVisibility(View.GONE);
                            lvGoods.setVisibility(View.VISIBLE);
                        }
                    }
                    goodsList.addAll(data.getData().getData());
                    //得到购物车中每件商品订购的数量
                    goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
                    myGoodslistAdapter.notifyDataSetChanged();

                    //获取总价钱
                    for (int i=0;i<goodsList.size();i++){
//                        price+=goodsNumList.get(i)*goodsList.get(i).getSMC_UnitPrice();
                        price = MyUtils.add(price, MyUtils.mul(goodsNumList.get(i), goodsList.get(i).getSMC_UnitPrice()));
                    }
                    setCartView(price);
                    pageIndex++;
                    dismissDialog();
                    break;
                case ConfirmCart_SUCCESS:
                    goTOCheckOut.setClickable(true);
                    dismissDialog();
                    Intent intent = new Intent(ctx, ConfirmOrderActivity.class);
                    intent.putExtra("shopSerialNo", shopSerialNo);
                    intent.putExtra("freight",freight);
                    startActivity(intent);
            }
        }
    };
    /**
     * 显示购物车
     */
    private void showShopCart() {
        dBgoodsDataList.clear();
        dBgoodsDataList.addAll(myDBHelper.getAllGoods());
        View view = View.inflate(ctx, R.layout.pop_shop_cart, null);
        final PopupWindow window = new PopupWindow(view,
                lvGoods.getWidth(),
                lvGoods.getHeight());
        window.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //得到购物车中每件商品订购的数量
                goodsNumList.clear();
                myGoodslistAdapter.setBuyNum(totalCount);
                goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
                myGoodslistAdapter.notifyDataSetChanged();
            }
        });
        window.setAnimationStyle(R.style.myShopCartPopStyle);
        window.showAtLocation(shopCartMain,
                Gravity.BOTTOM, 0, shopCartMain.getHeight());
        TextView tvShopCartClear = (TextView) view.findViewById(R.id.tvShopCartClear);
        ListView lvShopCart = (ListView) view.findViewById(R.id.lvShopCart);
        ShopCartAdapter shopCartAdapter = new ShopCartAdapter(dBgoodsDataList, ctx);
        shopCartAdapter.setOnShopCartGoodsChangeListener(new ShopCartAdapter.OnShopCartGoodsChangeListener() {
            @Override
            public void onNumAdd(int position) {
                DBgoodsData entity = dBgoodsDataList.get(position);
                myDBHelper.goodsAdd(entity);
                price =MyUtils.add(price, entity.getPriceUnit());
                setCartView(price);
                totalCount++;
                changeShopCart(totalCount);
            }

            @Override
            public void onNumDecrease(int position) {
                DBgoodsData entity = dBgoodsDataList.get(position);
                myDBHelper.goodsDecrease(entity);
                price = MyUtils.sub(price, entity.getPriceUnit());
                setCartView(price);
                totalCount--;
                changeShopCart(totalCount);
            }
        });
        lvShopCart.setAdapter(shopCartAdapter);
        tvShopCartClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearShopCart(window);
            }
        });
    }

    /**
     * 清空购物车
     */
    private void clearShopCart(final PopupWindow popupWindow) {
        MyUtils.showAlertDialog(ctx, "清空购物车", "确定清空购物车", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                myDBHelper.clearAllGoods();
                initCart();
                popupWindow.dismiss();
            }
        });
    }
    /**
     * 购物车显示,价格、去结算按钮的显示、还剩多少钱起送、的数据的设置
     * @param price
     */
    private void setCartView(double price){
        totalPrice.setText("¥" + price);
        if(price==0){
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            howMoneyToDelivery.setText("¥"+minMoney+"起送");
        }else if(price>0&&price < minMoney){
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            double f = MyUtils.sub(minMoney,price);
            howMoneyToDelivery.setText("还差¥"+f+"起送");
        }else if(price>=minMoney){
            howMoneyToDelivery.setVisibility(View.GONE);
            goTOCheckOut.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 得到添加购物车接口的一个参数CartList
     *
     * @param goodsList
     * @return
     */
    private String getStrCartList(List<DBgoodsData> goodsList) {
        List<Goods> datas = new ArrayList<>();
        for (DBgoodsData goods : goodsList) {
            Goods data = new Goods();
            data.setSMNo(goods.getNumberSM());
            data.setCommNo(goods.getNumberComm());
            data.setBuyNum(goods.getOrderCount());
            datas.add(data);
        }
        String str = datas.toString();
        LogUtils.e(str);
        return str;
    }
    private class Goods {
        private String SMNo;//超市编号
        private String CommNo;//商品编号
        private int BuyNum;//购买数量

        public String getSMNo() {
            return SMNo;
        }

        public void setSMNo(String SMNo) {
            this.SMNo = SMNo;
        }

        public String getCommNo() {
            return CommNo;
        }

        public void setCommNo(String commNo) {
            CommNo = commNo;
        }

        public int getBuyNum() {
            return BuyNum;
        }

        public void setBuyNum(int buyNum) {
            BuyNum = buyNum;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"SMNo\"" + ":\"" + SMNo + '\"' + ',' +
                    "\"CommNo\"" + ":\"" + CommNo + '\"' + ',' +
                    "\"BuyNum\"" + ":\"" + BuyNum + '\"' +
                    '}';
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //得到购物车中每件商品订购的数量
        goodsNumList.clear();
        myGoodslistAdapter.setBuyNum(totalCount);
        goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
        myGoodslistAdapter.notifyDataSetChanged();
    }

    /**
     * 输入监听
     */
    private class MyAddTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO: 2016/2/29 根据商品内容搜索商品
            LogUtils.outLog(s.toString().trim());
//            getGoods();
        }
    }
}
