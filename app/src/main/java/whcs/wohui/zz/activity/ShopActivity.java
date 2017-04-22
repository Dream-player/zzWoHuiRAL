package whcs.wohui.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import okhttp3.Call;
import whcs.wohui.zz.Bean.ShopDetailBean;
import whcs.wohui.zz.callback.ShopDetailCallBack;
import whcs.wohui.zz.fragment.BaseFragment;
import whcs.wohui.zz.fragment.ShopGoodsFragment;
import whcs.wohui.zz.fragment.ShopTabEvaluateFragment;
import whcs.wohui.zz.fragment.ShopTabGoodsFragment;
import whcs.wohui.zz.fragment.ShopTabStoreFragment;
import whcs.wohui.zz.url.ParamsKey;
import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.utils.MyRequestParams;
import whcs.wohui.zz.utils.ShopCartDatabaseHelper;
import whcs.wohui.zz.whcouldsupermarket.R;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;


/**
 * 说明：商家的类
 * 作者：陈杰宇
 * 时间： 2016/1/12 10:49
 * 版本：V1.0
 * 修改历史：
 */
public class ShopActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShopActivity";
    private RelativeLayout goBack;
    private TextView titleName;
    private Indicator indicator;
    private ViewPager viewPager;
//    private ImageView titleSearch;
    private RelativeLayout titleDoSearch;
    private TextView titleAll;
    private String shopSerialNo;
    private String numberCate="";
    private int pageIndexnum = 1;//商品列表的当前页数
    private int number=1;//控制加载全部商品

    private void assignViews() {
        titleDoSearch= (RelativeLayout) findViewById(R.id.titleDoSearch);
        titleAll = (TextView) findViewById(R.id.titleAll);
//        titleSearch = (ImageView) findViewById(R.id.ivTitleSearch);
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        indicator = (FixedIndicatorView) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private Context ctx;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private String[] shopTabName = {"商品","分类", "评价", "商家"};
    private BaseFragment[] shopTabFragment = {new ShopGoodsFragment(),
            new ShopTabGoodsFragment(),
            new ShopTabEvaluateFragment(),
            new ShopTabStoreFragment()};
    private MyIndicatorFragmentAdapter myIndicatorFragmentAdapter;
    private ShopCartDatabaseHelper myDBHelper;//购物车数据表工具
    private final static int SHOP_NAME_ERROR = 101;//成功请求到超市详情
    private final static int SHOP_NAME_RESPONT = 102;//请求超市详情失败
    public double getMinMoney() {
        return minMoney;
    }

    private double minMoney;//最低消费
    private MyOkHttpUtils myOkHttpUtils ;
    private MyRequestParams params;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private String shopName;


    private ShopDetailBean shopDetail;//商家详情
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop);
        ctx = this;
        Intent intent = getIntent();
        shopSerialNo = intent.getStringExtra("serialNumber");
        LogUtils.e(TAG,shopSerialNo);
        minMoney = intent.getDoubleExtra("MinMoney", 20);
        shopName = intent.getStringExtra("shopName");
        //进入该页面，创建对应数据表
        myDBHelper = getMyApplication().getMyDBHelper(shopSerialNo);
        myDBHelper.createTable();
        assignViews();

        initView();
        getShopDetail(shopSerialNo);
    }

    /**
     * 进行网络请求，通过超市编号得到超市详情
     */
    private void getShopDetail(String numberSM){
        showDialog(ctx);
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.ShopSerialNumber, numberSM);
        String strUrl= Urls.GetShopDetail;
        myOkHttpUtils.postRequest(strUrl, params, new ShopDetailCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                handler.sendEmptyMessage(SHOP_NAME_ERROR);
            }

            @Override
            public void onResponse(ShopDetailBean response) {
                if (response.getState() == 1) {
                    String str = response.getData().getS_Name();
                    Message msg = new Message();
                    msg.what = SHOP_NAME_RESPONT;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }

            }

        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOP_NAME_ERROR://得到超市详情失败
                    Toast.makeText(ctx, "网络错误", Toast.LENGTH_SHORT).show();
                    dismissDialog();
                    break;
                case SHOP_NAME_RESPONT://网络请求得到超市详情
                    shopDetail = (ShopDetailBean) msg.obj;
                    LogUtils.e(TAG,shopDetail.getData().getS_GUID());
                    dismissDialog();
                    break;
            }
        }
    };
    /**
     * 给控件设置监听，添加适配器
     */
    private void initView(){
        indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.mainColor), 5));
        indicator.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(getResources().getColor(R.color.mainColor),
                        getResources().getColor(R.color.color_666)));
        // 将viewPager和indicator使用
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setPageOffscreenLimit(5);
        inflate = LayoutInflater.from(getApplicationContext());
        myIndicatorFragmentAdapter = new MyIndicatorFragmentAdapter(getSupportFragmentManager());
        // 设置indicatorViewPager的适配器
        indicatorViewPager.setAdapter(myIndicatorFragmentAdapter);
        titleName.setText(shopName);
        titleDoSearch.setVisibility(View.VISIBLE);
        titleAll.setOnClickListener(this);
//        titleSearch.setVisibility(View.VISIBLE);
//        titleSearch.setOnClickListener(this);
        goBack.setOnClickListener(this);
    }

    private class MyIndicatorFragmentAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyIndicatorFragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.shop_tab, container, false);
            }
            TextView tvTabName = (TextView) convertView;
            tvTabName.setText(shopTabName[position]);
            return tvTabName;
        }

        @Override
        public Fragment getFragmentForPage(int i) {

            BaseFragment fragment = shopTabFragment[i];

            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof ShopGoodsFragment) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                ShopActivity.this.onBackPressed();
                break;
            case R.id.ivTitleSearch:
//                searchGoods();
                setnumber(1);
                indicatorViewPager.setCurrentItem(0,true);
                myIndicatorFragmentAdapter.notifyDataSetChanged();
//                activity.getindicatorViewPager().setCurrentItem(0,true);
//                activity.getmyIndicatorFragmentAdapter().notifyDataSetChanged();
                break;

            case R.id.titleAll:
//                searchGoods();
                setnumber(1);
                indicatorViewPager.setCurrentItem(0,true);
                myIndicatorFragmentAdapter.notifyDataSetChanged();
//                activity.getindicatorViewPager().setCurrentItem(0,true);
//                activity.getmyIndicatorFragmentAdapter().notifyDataSetChanged();
                break;
        }
    }

    /**
     * 跳转搜索商品页面
     */
    private void searchGoods() {
        Intent intent = new Intent(ctx, ShopSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("shopSerialNo", shopSerialNo);
        intent.putExtra("MinMoney", minMoney);

        intent.putExtra("freight",shopDetail.getData().getS_Freight());
        startActivity(intent);
    }

    public String getShopSerialNo() {
        return shopSerialNo;
    }
    public ShopDetailBean getShopDetail() {
        return shopDetail;
    }

    public IndicatorViewPager getindicatorViewPager() {
        return indicatorViewPager;
    }

    public IndicatorViewPager.IndicatorFragmentPagerAdapter getmyIndicatorFragmentAdapter() {
        return myIndicatorFragmentAdapter;
    }

    public void setnumberCate(String numberCate) {
        this.numberCate = numberCate;
    }

    public String getnumberCate() {
        return numberCate;
    }

    public void setpageIndexnum(int pageIndexnum) {
        this.pageIndexnum = pageIndexnum;
    }

    public int getpageIndexnum() {
        return pageIndexnum;
    }
    public void setnumber(int number) {
        this.number = number;
    }

    public int getnumber() {
        return number;
    }

}
