package whcs.wohui.zz.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.adapter.CBPageAdapter;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import whcs.wohui.zz.Bean.AdListBean;
import whcs.wohui.zz.Bean.GetNoticeBean;
import whcs.wohui.zz.Bean.GoodsListBean;
import whcs.wohui.zz.Bean.GoodsListBean.DataEntity.GoodsEntity;
import whcs.wohui.zz.Bean.NearbyShopBean;
import whcs.wohui.zz.activity.AdActivity;
import whcs.wohui.zz.activity.ShopActivity;
import whcs.wohui.zz.adapter.NearbyGoodsAdapter;
import whcs.wohui.zz.callback.AdListCallBack;
import whcs.wohui.zz.callback.GetNoticeCallBack;
import whcs.wohui.zz.callback.GoodsListCallBack;
import whcs.wohui.zz.callback.NearbyShopCallBack;
import whcs.wohui.zz.listener.SwipeListViewOnScrollListener;
import whcs.wohui.zz.myview.MyAlwaysRunTextView;
import whcs.wohui.zz.service.LocationService;
import whcs.wohui.zz.url.ParamsKey;
import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.utils.MyRequestParams;
import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.MyApplication;
import whcs.wohui.zz.whcouldsupermarket.R;


/**
 * 说明：首页
 * 作者：刘志海
 * 时间： 2016/4/12 15:56
 * 版本：
 * 修改历史：
 */
public class FirstFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{



    private Context ctx;
    private MainActivity activity;
    private ConvenientBanner convenientBanner;
    private MyOkHttpUtils myOkHttpUtils;
    private BDLocation location;
    private SwipeRefreshLayout swipeRefLayout;
    private NearbyGoodsAdapter goodsListAdapter;
    private ListView listView;
    private PullToRefreshListView mainPullList;
    private TextView textView;
    private MyAlwaysRunTextView userLocationAdd;
    private LinearLayout icon1;
    private LinearLayout icon2;
    private LinearLayout icon3;
    private LinearLayout icon4;
    private LinearLayout icon5;
    private LinearLayout icon6;
    private LinearLayout icon7;
    private LinearLayout icon8;


    private double latitude;
    private double longitude;
    private LocationService locService;
    private static final int NS_CALLBACK_SUCCESS = 10003;//附近商家请求成功
    private static final int NS_CALLBACK_DEFEAT = 10004;//附近商家请求失败
    private static final int GN_CALLBACK_SUCCESS = 10005;//附近商家请求成功
    private static final int GN_CALLBACK_DEFEAT = 10006;//附近商家请求失败
    private static final int GET_GL_SUCCESS = 1100;//得到商品成功
    private static final int GET_GL_DEFEAT = 1101;//得到商品失败
    private static final int ImagesRequest_OK = 4984;
    private SwipeListViewOnScrollListener myListScrollListener;
    private MyRequestParams params = new MyRequestParams();

    private List<GoodsEntity> goodsList=new ArrayList<>();
    private List<NearbyShopBean.DataEntity> shopEntityList = new ArrayList<>();
    private List<String> imageList1 = new ArrayList<>();
    private String shopnumer1;

    private void assignViews(View v) {

        swipeRefLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefLayout);
        mainPullList = (PullToRefreshListView) v.findViewById(R.id.mainPullList1);
        listView = mainPullList.getRefreshableView();


    }

    @Override
    public View initView() {
        ctx = FirstFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.shouye_fragment, null);
        myOkHttpUtils = new MyOkHttpUtils(ctx);
        assignViews(v);

        addListHead();
        addListFoot();

        initThisData();
        initListener();

        imagesADRequest();
//        textADRequest(shopnumer1);
        return v;
    }

    private void addListHead() {
        View v = LayoutInflater.from(ctx).inflate(R.layout.first_list_head, listView, false);
        convenientBanner = (ConvenientBanner) v.findViewById(R.id.convenientBanner);
        userLocationAdd = (MyAlwaysRunTextView) v.findViewById(R.id.userLocationAdd1);
        icon1= (LinearLayout) v.findViewById(R.id.yunjie_first_icon);
        icon2= (LinearLayout) v.findViewById(R.id.yundian_first_icon);
        icon3= (LinearLayout) v.findViewById(R.id.yunyou_first_icon);
        icon4= (LinearLayout) v.findViewById(R.id.yunlian_first_icon);
        icon5= (LinearLayout) v.findViewById(R.id.yunshang_first_icon);
        icon6= (LinearLayout) v.findViewById(R.id.qiangbao_first_icon);
        icon7= (LinearLayout) v.findViewById(R.id.chongzhi_first_icon);
        icon8= (LinearLayout) v.findViewById(R.id.xinwen_first_icon);

//        rlNoData = (LinearLayout) v.findViewById(R.id.rl_no_data);
        listView.addHeaderView(v);
    }
    private void addListFoot() {
        View v = LayoutInflater.from(ctx).inflate(R.layout.first_list_foot, listView, false);
        textView = (TextView) v.findViewById(R.id.di_text);

//        rlNoData = (LinearLayout) v.findViewById(R.id.rl_no_data);
        listView.addFooterView(v);
    }

    /**
     * 获取轮播图的图片地址
     */
    private void imagesADRequest() {
        okHttpGet(Urls.GetAdList, new AdListCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(AdListBean response) {
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.what = ImagesRequest_OK;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 网络请求获得滚动字幕
     *
     * @param shopnumber
     *
     */
    private void textADRequest(String shopnumber) {
//        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.ShopSerialNumber, shopnumber);
        String strUrl = Urls.GetNotice;
        myOkHttpUtils.postRequest(strUrl, params, new GetNoticeCallBack(){
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(ctx, "网络请求失败", Toast.LENGTH_SHORT);
                swipeRefLayout.setRefreshing(false);
                handler.sendEmptyMessage(GN_CALLBACK_DEFEAT);

            }

            @Override
            public void onResponse(GetNoticeBean response) {
                swipeRefLayout.setRefreshing(false);
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = GN_CALLBACK_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    Toast.makeText(ctx, "返回网络状态" + response.getState(), Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }
            }
        });

    }

    private void initListener() {

        mainPullList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.outLog("checked");
                return false;
            }
        });
        listView.setAdapter(goodsListAdapter);
//        给listView设置item监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("点击位置:"+position);
                int i = position - 2;
                Intent intent = new Intent();
                if (!isLogin()) {
                    LogUtils.e("login");
                    login(intent);
                } else {
                    intent.setClass(ctx, ShopActivity.class);
                    NearbyShopBean.DataEntity data = shopEntityList.get(0);
                    intent.putExtra("serialNumber", data.getS_GUID());
                    intent.putExtra("MinMoney", data.getS_MinMoney());
                    intent.putExtra("shopName", data.getS_Name());
                    LogUtils.e(position + "");
                    startActivity(intent);
                }

            }
        });

        icon1.setOnClickListener(this);
        icon2.setOnClickListener(this);
        icon3.setOnClickListener(this);
        icon4.setOnClickListener(this);
        icon5.setOnClickListener(this);
        icon6.setOnClickListener(this);
        icon7.setOnClickListener(this);
        icon8.setOnClickListener(this);
    }

    private void initThisData() {

        swipeRefLayout.setColorSchemeResources(R.color.homeFragmentRefreshColor);
//        MyUtils.initPullList(mainPullList, ctx);

        goodsListAdapter = new NearbyGoodsAdapter(goodsList, ctx);

        myListScrollListener = new SwipeListViewOnScrollListener(swipeRefLayout);
        mainPullList.setOnScrollListener(myListScrollListener);

        //TODO 设置上拉刷新
//        myPullToRefreshListener = new MyPullToRefreshListener();
//        mainPullList.setOnRefreshListener(myPullToRefreshListener);
        swipeRefLayout.setOnRefreshListener(this);



        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//进行权限判断
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    33);
        }else {
            doLocation();
        }
    }

    @Override
    public void initEachData() {

    }

    /**
     * 给ListView添加头部,并分别为头部与它自身添加item监听
     */
    private void initListHead(List<String> imageList) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, imageList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.startTurning(3000);
        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent= new Intent();
                intent.setClass(ctx, AdActivity.class);
                intent.putExtra("position", position+"");
                LogUtils.e(position + "");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRefresh() {
        imagesADRequest();
        getGoods(latitude,longitude);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yunjie_first_icon:
                openBrower("http://yj.wohui365.com");
                break;
            case R.id.yundian_first_icon:
                openBrower("http://yd.wohui365.com");
                break;
            case R.id.yunyou_first_icon:
                openBrower("http://yly.wohui365.com");
                break;
            case R.id.yunlian_first_icon:
                openBrower("http://f.yd.wohui365.com");
                break;
            case R.id.yunshang_first_icon:
                openBrower("http://f.yd.wohui365.com/m/index.php?mod=mall&act=index");
                break;
            case R.id.qiangbao_first_icon:
                openBrower("http://qb.wohui365.com");
                break;
            case R.id.chongzhi_first_icon:
                Toast.makeText(ctx, "开发中，敬请期待", Toast.LENGTH_SHORT).show();
//                openBrower("https://www.baidu.com/");
                break;
            case R.id.xinwen_first_icon:
                openBrower("http://www.wohuijituan.com/news/news.html");
                break;
        }
    }

    /**
     * convenientBanner加载图片
     */
    public class LocalImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        public void UpdateUI(Context context, final int position, String data) {
            myOkHttpUtils.downLoadImage(data, imageView);
        }
    }

    /**
     * 进行定位
     */
    private void doLocation() {
        showDialog(ctx);
        LogUtils.outLog("doLocation()");
        locService = ((MyApplication) activity.getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
    }



    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location1) {
            LogUtils.e("定位类型：" + location1.getLocType());

            if (location1 != null && (location1.getLocType() == 161 || location1.getLocType() == 66)) {
                location = location1;

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LogUtils.e("TAG","latitude和longitude是"+latitude+","+longitude);

//                EventBus.getDefault().post(location);
                dismissDialog();
                ((MainActivity)getActivity()).setmLocation(location);

                getGoods(latitude,longitude);
                getNearbyShop(latitude,longitude);

            } else {
                Toast.makeText(ctx, "定位失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 33) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doLocation();
            } else {
                showToast(ctx,"请设置允许定位权限!");
            }
        }
    }

    /**
     * 网络请求获得附近商家
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    private void getNearbyShop(double latitude, double longitude) {
//        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.NearbyShop_Latitude, latitude + "");
        params.addStringRequest(ParamsKey.NearbyShop_Longitude, longitude + "");
        String strUrl = Urls.GetNearbyShop;
        myOkHttpUtils.postRequest(strUrl, params, new NearbyShopCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(ctx, "网络请求失败", Toast.LENGTH_SHORT);
                swipeRefLayout.setRefreshing(false);
                handler.sendEmptyMessage(NS_CALLBACK_DEFEAT);

            }

            @Override
            public void onResponse(NearbyShopBean response) {
                swipeRefLayout.setRefreshing(false);
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = NS_CALLBACK_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    Toast.makeText(ctx, "返回网络状态" + response.getState(), Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }
            }
        });

    }

    /**
     * 网络请求获取商品
     */
    private void getGoods(double latitude,double longitude) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.NearbyShop_Latitude, latitude+"");
        params.addStringRequest(ParamsKey.NearbyShop_Longitude, longitude+"");

        String strUrl = Urls.NearSMCommList;
        myOkHttpUtils.postRequest(strUrl, params, new GoodsListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                swipeRefLayout.setRefreshing(false);
                handler.sendEmptyMessage(GET_GL_DEFEAT);
            }

            @Override
            public void onResponse(GoodsListBean response) {
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = GET_GL_SUCCESS;
                    swipeRefLayout.setRefreshing(false);
                    handler.sendMessage(msg);
                }
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
                    goodsList.clear();
                    goodsList.addAll(data.getData().getData());
                    goodsListAdapter.notifyDataSetChanged();

                    Toast.makeText(ctx, "商品刷新完成", Toast.LENGTH_SHORT).show();
                    dismissDialog();
                    break;
                case ImagesRequest_OK:
                    AdListBean adListData = (AdListBean) msg.obj;
                    // 创建一个list存放imageURl
                    List<String> imageList = new ArrayList<>();

                    for (AdListBean.DataEntity dataEntity : adListData.getData()) {
//                        imageList.add(dataEntity.getAD_ImgUrl());
//                        imageList1.add(dataEntity.getAD_LinkUrl());
                    }
                    imageList.add("http://img10.360buyimg.com/cms/jfs/t3163/17/1319630042/109911/4d61d2e0/57c94d2cN0928fc96.jpg");
                    imageList.add("http://img11.360buyimg.com/cms/jfs/t3304/208/1257512630/85008/8158e0f/57c94d41Nb1910bf7.jpg");
                    imageList.add("http://img11.360buyimg.com/cms/jfs/t3019/227/1696083172/101137/8f0d5551/57c94d5cN315fd854.jpg");
                    imageList.add("http://img10.360buyimg.com/cms/jfs/t3301/335/1291165781/347071/c6d47d5c/57c94d72N0080bb9a.jpg");

                    initListHead(imageList);
                    break;
                case NS_CALLBACK_DEFEAT:
//                    dismissDialog();
                    break;
                case NS_CALLBACK_SUCCESS:
                    LogUtils.e("附近商家请求成功");
                    NearbyShopBean data1 = (NearbyShopBean) msg.obj;
                    if (data1.getData().size() == 0) {
//                        rlNoData.setVisibility(View.VISIBLE);
//                        Toast.makeText(ctx, "附近暂无商家", Toast.LENGTH_SHORT).show();
                    } else {
//                        rlNoData.setVisibility(View.GONE);
//                        Toast.makeText(ctx, "附近商家请求成功", Toast.LENGTH_SHORT).show();

                    }
                    shopEntityList.clear();
                    shopEntityList.addAll(data1.getData());
                    shopnumer1 = shopEntityList.get(0).getS_GUID();
                    textADRequest(shopnumer1);

                    break;

                case GN_CALLBACK_DEFEAT:
//                    dismissDialog();
                    break;
                case GN_CALLBACK_SUCCESS:
                    LogUtils.e("附近商家请求成功");
                    GetNoticeBean data2 = (GetNoticeBean) msg.obj;
                    String num=data2.getData();
                    userLocationAdd.setText(num);
                    break;
            }
        }
    };
    /**
     * 打开浏览器
     *
     * @param url
     */
    private void openBrower(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.outLog("onDestroy");
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }
    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }
}