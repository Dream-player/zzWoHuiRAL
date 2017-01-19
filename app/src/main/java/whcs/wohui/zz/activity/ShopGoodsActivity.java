package whcs.wohui.zz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
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
 * 说明：商店商品列表
 * 作者：陈杰宇
 * 时间： 2016/2/28 22:51
 * 版本：V1.0
 * 修改历史：
 */
public class ShopGoodsActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView lvGoods;
    private TextView titleName;
    private RelativeLayout goBack;
    private ImageView ivTitleSearch;
    private RelativeLayout shopCartMain;
    private ImageView image;
    private TextView shopCartNum;
    private TextView totalPrice;
    private TextView howMoneyToDelivery;
    private TextView goTOCheckOut;
    private String numberCate;
    private String shopName;


    private void assignViews() {
        lvGoods = (PullToRefreshListView) findViewById(R.id.lvGoods);
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        ivTitleSearch = (ImageView) findViewById(R.id.ivTitleSearch);
        shopCartMain = (RelativeLayout) findViewById(R.id.shopCartMain);
        image = (ImageView) findViewById(R.id.image);
        shopCartNum = (TextView) findViewById(R.id.shopCartNum);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        howMoneyToDelivery = (TextView) findViewById(R.id.howMoneyToDelivery);
        goTOCheckOut = (TextView) findViewById(R.id.goTOCheckOut);
    }

    private Context ctx;
    private List<GoodsEntity> goodsList;
    private ShopCartDatabaseHelper myDBHelper;//购物车数据库工具类
    private String shopSerialNo;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private int pageIndex = 1;//商品列表的当前页数
    private static final int GET_GL_SUCCESS = 1100;//得到商品成功
    private static final int GET_GL_DEFEAT = 1101;//得到商品失败
    private static final int ConfirmCart_SUCCESS = 7785;//确认购物车成功
    private GoodsListAdapter goodsListAdapter;
    private double price = 0;//购买总钱数
    private List<Integer> goodsNumList;//订购的商品数量
    private double minMoney;
    private double freight;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_goods);
        assignViews();

        ctx = this;
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        //根据获取到的信息填充标题等，根据超市标号获取数据库工具类
        Intent intent = getIntent();
        numberCate = intent.getStringExtra("numberCate");
        shopSerialNo = intent.getStringExtra("shopSerialNo");
        shopName = intent.getStringExtra("shopName");
        minMoney = intent.getDoubleExtra("minMoney",0);
        freight = intent.getDoubleExtra("freight",0);
        myDBHelper = new ShopCartDatabaseHelper(getMyApplication().getUserKey(), shopSerialNo, ctx);
        initData();
        initListener();
        // 联网获数据
        getGoods(shopSerialNo, pageIndex, numberCate);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        ivTitleSearch.setOnClickListener(this);
        shopCartMain.setOnClickListener(this);
        goTOCheckOut.setOnClickListener(this);
        howMoneyToDelivery.setOnClickListener(this);
        goBack.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        MyUtils.initPullList(lvGoods, ctx);
        titleName.setText(shopName);
        ivTitleSearch.setVisibility(View.VISIBLE);
        goodsList = new ArrayList<>();
        dBgoodsDataList = new ArrayList<>();
        goodsNumList = new ArrayList<>();
        goodsListAdapter = new GoodsListAdapter(goodsList, ctx, myDBHelper);
        goodsListAdapter.setmActivity(this);
        goodsListAdapter.setShopCart(shopCartNum);
        goodsListAdapter.setGoodsNum(goodsNumList);
        goodsListAdapter.setOnShopCartGoodsChangeListener(new GoodsListAdapter.OnShopCartGoodsChangeListener() {
            @Override
            public void onNumAdd(int position) {
                totalCount++;
                GoodsEntity entity = goodsList.get(position);
                myDBHelper.goodsAdd(entity);
                price += entity.getSMC_UnitPrice();
                setCartViewPrice(price);

            }

            @Override
            public void onNumDecrease(int position) {
                totalCount--;
                GoodsEntity entity = goodsList.get(position);
                myDBHelper.goodsDecrease(entity);
                price -= entity.getSMC_UnitPrice();
                setCartViewPrice(price);
            }
        });
        lvGoods.setAdapter(goodsListAdapter);
        lvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getGoods(shopSerialNo, pageIndex, numberCate);
            }
        });
    }

    /**
     * 网络请求获取商品
     */
    private void getGoods(String shopSerialNo, int pageIndex, String numberCate) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.GetGoods_goodsOrder, "1");
        params.addStringRequest(ParamsKey.GetGoods_shopSerialNo, shopSerialNo);
        params.addStringRequest(ParamsKey.GetGoods_pageIndex, "" + pageIndex);
        params.addStringRequest(ParamsKey.GetGoods_CateNo,numberCate);
        params.addStringRequest(ParamsKey.GetGoods_pageSize, "10");
        String strUrl = Urls.GetGoodsList;
        myOkHttpUtils.postRequest(strUrl, params, new GoodsListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.ivTitleSearch://进入搜索页面
                searchGoods();
                break;
            case R.id.shopCartMain://显示购物车,弹窗
                showShopCart();
                break;
            case R.id.goTOCheckOut://去结算按钮
                //去结算的时候，先进行清空后台购物车的操作
                //清空之后，再进行向后台添加当前购物车数据库中的商品
//                clearShopCart(shopSerialNo);
                checkOut(shopSerialNo,0);
                break;
            case R.id.titleGoBack:
                this.onBackPressed();
                finish();
                break;
        }
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
//                        handler.sendEmptyMessageDelayed(ConfirmCart_SUCCESS, 500);
                }else {
                    showToast(ctx, "添加失败:" + response.getData().getMessage());
                    dismissDialog();
                    goTOCheckOut.setClickable(true);
                }
            }
        });
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

    /**
     * 跳转到搜索商品界面
     */
    private void searchGoods() {
        Intent intent = new Intent(ctx, ShopSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("shopSerialNo", shopSerialNo);
        intent.putExtra("MinMoney", minMoney);
        intent.putExtra("freight",freight);
        startActivity(intent);
    }

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
        window.setAnimationStyle(R.style.myShopCartPopStyle);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //初始化物品列表每一条物品的添加总数
                goodsNumList.clear();
                goodsListAdapter.setBuyNum(totalCount);
                goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
                goodsListAdapter.notifyDataSetChanged();
            }
        });
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
                price += entity.getPriceUnit();
                setCartView(price);
                totalCount++;
                changeShopCart(totalCount);
            }

            @Override
            public void onNumDecrease(int position) {
                DBgoodsData entity = dBgoodsDataList.get(position);
                myDBHelper.goodsDecrease(entity);
                price -= entity.getPriceUnit();
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
     * 清空本地数据库中的购物车
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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_GL_DEFEAT:
                    Toast.makeText(ctx, "网络请求失败", Toast.LENGTH_SHORT).show();
                    dismissDialog();
                    break;
                case GET_GL_SUCCESS:
                    GoodsListBean data = (GoodsListBean) msg.obj;//获取网络请求商品列表
                    if (pageIndex == 1) {
                        goodsList.clear();
                        goodsNumList.clear();
                    }
                    goodsList.addAll(data.getData().getData());
                    //得到购物车中每件商品订购的数量
                    goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
                    goodsListAdapter.notifyDataSetChanged();
                    pageIndex++;
                    dismissDialog();
                    break;
                case ConfirmCart_SUCCESS:
                    dismissDialog();
                    goTOCheckOut.setClickable(true);
                    Intent intent = new Intent(ctx, ConfirmOrderActivity.class);
                    intent.putExtra("shopSerialNo", shopSerialNo);
                    startActivity(intent);
            }
        }
    };


    private List<DBgoodsData> dBgoodsDataList;//购物车数据库商品列表
    private int totalCount;//购物车商品总数量


    /**
     * 初始化购物车界面
     */
    private void initCart() {
        price = 0;
        totalCount = 0;
        dBgoodsDataList.clear();
        dBgoodsDataList.addAll(myDBHelper.getAllGoods());
        for (DBgoodsData data : dBgoodsDataList) {
            totalCount += data.getOrderCount();//初始化购物车商品总数
            price += data.getOrderCount() * data.getPriceUnit();//初始化购物车商品总价
        }
        setCartView(price);//给底部购物车界面设置总价钱
        changeShopCart(totalCount);// 给底部购物车界面设置总数量
        LogUtils.e("初始化购物车界面initCart()");
    }

    /**
     * 设置购物车界面的数量显示
     *
     * @param totalCount
     */
    private void changeShopCart(int totalCount) {
        if (totalCount > 0) {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(totalCount + "");
        } else {
            shopCartNum.setVisibility(View.GONE);
        }
    }

    /**
     * 购物车显示的数据的设置
     *
     * @param price
     */
    private void setCartViewPrice(double price) {
        totalPrice.setText("¥" + price);
        if (price == 0) {
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            howMoneyToDelivery.setText("¥"+minMoney+"免送");
        } else if (price > 0 && price < minMoney) {
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            double f = MyUtils.sub(minMoney,price);
            howMoneyToDelivery.setText("还差¥" + f + "免送");
        } else if (price >= minMoney) {
            howMoneyToDelivery.setVisibility(View.GONE);
            goTOCheckOut.setVisibility(View.VISIBLE);
        }
//        totalPrice.setText("¥" + price);
//        if (price == 0) {
//            howMoneyToDelivery.setVisibility(View.VISIBLE);
//            goTOCheckOut.setVisibility(View.GONE);
//            howMoneyToDelivery.setText("¥20起送");
//        } else if (price > 0 && price < 20) {
//            howMoneyToDelivery.setVisibility(View.VISIBLE);
//            goTOCheckOut.setVisibility(View.GONE);
//            double f = 20 - price;
//            howMoneyToDelivery.setText("还差¥" + f + "起送");
//        } else if (price >= 20) {
//            howMoneyToDelivery.setVisibility(View.GONE);
//            goTOCheckOut.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * 设置购物车界面的价格显示
     *
     * @param price
     */
    private void setCartView(double price) {
        totalPrice.setText("¥" + price);
        if (price == 0) {
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            howMoneyToDelivery.setText("¥"+minMoney+"免送");
        } else if (price > 0 && price < minMoney) {
            howMoneyToDelivery.setVisibility(View.VISIBLE);
            goTOCheckOut.setVisibility(View.GONE);
            double f = MyUtils.sub(minMoney,price);
            howMoneyToDelivery.setText("还差¥" + f + "免送");
        } else if (price >= minMoney) {
            howMoneyToDelivery.setVisibility(View.GONE);
            goTOCheckOut.setVisibility(View.VISIBLE);
        }
//        totalPrice.setText("¥" + price);
//        if (price == 0) {
//            howMoneyToDelivery.setVisibility(View.VISIBLE);
//            goTOCheckOut.setVisibility(View.GONE);
//            howMoneyToDelivery.setText("¥20起送");
//        } else if (price > 0 && price < 20) {
//            howMoneyToDelivery.setVisibility(View.VISIBLE);
//            goTOCheckOut.setVisibility(View.GONE);
//            double f = 20 - price;
//            howMoneyToDelivery.setText("还差¥" + f + "起送");
//        } else if (price > 20) {
//            howMoneyToDelivery.setVisibility(View.GONE);
//            goTOCheckOut.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化购物车界面
        initCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化物品列表每一条物品的添加总数
        goodsNumList.clear();
        goodsListAdapter.setBuyNum(totalCount);
        goodsNumList.addAll(myDBHelper.getGoodsOrderCount(goodsList));
        goodsListAdapter.notifyDataSetChanged();
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
}
