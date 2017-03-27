package whcs.wohui.zz.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.adapter.CBPageAdapter;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import whcs.wohui.zz.Bean.AdListBean;
import whcs.wohui.zz.Bean.NearbyShopBean;
import whcs.wohui.zz.activity.BDMapActivity;
import whcs.wohui.zz.activity.OkHttpTestActivity;
import whcs.wohui.zz.activity.SearchLocationActivity;
import whcs.wohui.zz.activity.ShopActivity;
import whcs.wohui.zz.adapter.NearbyShopAdapter;
import whcs.wohui.zz.callback.AdListCallBack;
import whcs.wohui.zz.callback.NearbyShopCallBack;
import whcs.wohui.zz.listener.SwipeListViewOnScrollListener;
import whcs.wohui.zz.myview.MyAlwaysRunTextView;
import whcs.wohui.zz.service.LocationService;
import whcs.wohui.zz.url.ParamsKey;
import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyKey;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.utils.MyRequestParams;
import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.MyApplication;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：主页面——首页
 * 作者：陈杰宇
 * 时间： 2016/1/7 15:56
 * 版本：V1.0
 * 修改历史：2016-1-8 14:55:19 添加布局
 * 2016-1-13 13:55:44 将跟布局修改为OverScrollView
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout title;
    private ImageButton actionToMap;
    private SwipeRefreshLayout swipeRefLayout;
    private PullToRefreshListView mainPullList;
    private MyAlwaysRunTextView userLocationAdd;
    private BDLocation location;
    private LinearLayout rlNoData;

    private void assignViews(View v) {
        title = (LinearLayout) v.findViewById(R.id.title);
        actionToMap = (ImageButton) v.findViewById(R.id.actionToMap);
        swipeRefLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefLayout);
        mainPullList = (PullToRefreshListView) v.findViewById(R.id.mainPullList);
        userLocationAdd = (MyAlwaysRunTextView) v.findViewById(R.id.userLocationAdd);
        listView = mainPullList.getRefreshableView();

    }

    private Context ctx;
    private NearbyShopAdapter mAdapter;
    private ListView listView;
    private SwipeListViewOnScrollListener myListScrollListener;
    private MyPullToRefreshListener myPullToRefreshListener;
    private ConvenientBanner convenientBanner;
    private List<Integer> imageUrlList;
    private double latitude;
    private double longitude;
    private List<NearbyShopBean.DataEntity> shopEntityList = new ArrayList<>();
    private String[] images = {
            "http://img10.360buyimg.com/cms/jfs/t3163/17/1319630042/109911/4d61d2e0/57c94d2cN0928fc96.jpg",
            "http://img11.360buyimg.com/cms/jfs/t3304/208/1257512630/85008/8158e0f/57c94d41Nb1910bf7.jpg",
            "http://img11.360buyimg.com/cms/jfs/t3019/227/1696083172/101137/8f0d5551/57c94d5cN315fd854.jpg",
            "http://img10.360buyimg.com/cms/jfs/t3301/335/1291165781/347071/c6d47d5c/57c94d72N0080bb9a.jpg",
    };


    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params = new MyRequestParams();
    private LocationService locService;
    private MainActivity activity;
    private static final int NS_CALLBACK_SUCCESS = 10003;//附近商家请求成功
    private static final int NS_CALLBACK_DEFEAT = 10004;//附近商家请求失败
    public static final int ACTIVITY_LOCATION = 1;//定位页面返回来


    @Override
    public View initView() {
        ctx = HomeFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.home_fragment, null);
        assignViews(v);
        addListHead();
        initThisData();
        initListener();
        myOkHttpUtils = new MyOkHttpUtils(ctx);
        params = new MyRequestParams();
        EventBus.getDefault().register(this);
        imagesADRequest();
        return v;
    }

    private void initListener() {
        title.setOnClickListener(this);
        mainPullList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.outLog("checked");
                return false;
            }
        });
        listView.setAdapter(mAdapter);
        //给listView设置item监听
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
                    NearbyShopBean.DataEntity data = shopEntityList.get(i);
                    intent.putExtra("serialNumber", data.getS_GUID());
                    intent.putExtra("MinMoney", data.getS_MinMoney());
                    intent.putExtra("shopName", data.getS_Name());
                    LogUtils.e(position + "");
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * 初始化view但是切换页面的时候不会重绘此页面
     */
    private void initThisData() {
        swipeRefLayout.setColorSchemeResources(R.color.homeFragmentRefreshColor);
//        MyUtils.initPullList(mainPullList, ctx);

        mAdapter = new NearbyShopAdapter(shopEntityList, ctx);

        actionToMap.setOnClickListener(this);
        myListScrollListener = new SwipeListViewOnScrollListener(swipeRefLayout);
        mainPullList.setOnScrollListener(myListScrollListener);
        //TODO 设置上拉刷新
//        myPullToRefreshListener = new MyPullToRefreshListener();
//        mainPullList.setOnRefreshListener(myPullToRefreshListener);
        swipeRefLayout.setOnRefreshListener(this);
//        userLocationAdd.setOnClickListener(this);

//        if (activity.application.getCity() != null && !activity.application.getCity().equals("")) {
//            userLocationAdd.setText(activity.application.getAddress());
//            //获得经纬度进行附近商家列表请求
//            LatLng latLng = activity.application.getLatLng();
//            latitude = latLng.latitude;
//            longitude = latLng.longitude;
////            getNearbyShop(latitude,longitude);
//        } else {
//
//        }
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//进行权限判断
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    33);
        }else {
            doLocation();
        }
    }
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
     * 需要每次切换都进行初始化的在此方法中进行
     */
    @Override
    public void initEachData() {

    }



    private static final int ImagesRequest_OK = 4984;
    private static final int ImagesRequest_NULL = 4984;

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

    private void addListHead() {
        LayoutInflater lif = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = LayoutInflater.from(ctx).inflate(R.layout.home_list_head, listView, false);
        convenientBanner = (ConvenientBanner) v.findViewById(R.id.convenientBanner);

        rlNoData = (LinearLayout) v.findViewById(R.id.rl_no_data);
        listView.addHeaderView(v);
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
        convenientBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeRefLayout.setEnabled(false);
                LogUtils.outLog("checked");
                return true;
            }
        });

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

    /**
     * 跳转到OkHttpTestActivity
     */
    private void jump2OkHttpTest() {
        Intent intent = new Intent(ctx, OkHttpTestActivity.class);
        startActivity(intent);
    }

    /**
     * EventBus 处理消息的类
     *
     * @param event 接收的消息对象
     */
    public void onEventMainThread(Object event) {

        if (event instanceof String) {
            LogUtils.e("定位" + event.toString());
        } else if (event instanceof BDLocation) {
            BDLocation location = (BDLocation) event;
            String locationStr = location.getAddrStr();
            LogUtils.e("TAG","locationStr:"+locationStr);
            userLocationAdd.setText(locationStr);

            //获得经纬度进行附近商家列表请求
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            activity.application.setAddress(location.getAddrStr());
            activity.application.setLatLng(new LatLng(latitude, longitude));
            activity.application.setCity(location.getCity());
            getNearbyShop(latitude, longitude);
        }

    }

    /**
     * 网络请求获得附近商家
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    private void getNearbyShop(double latitude, double longitude) {
        showDialog(ctx);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionToMap:
                Intent iniToMap = new Intent(ctx, BDMapActivity.class);
                startActivity(iniToMap);
                break;
//            case R.id.userLocationAdd:
//                Intent locationIntent = new Intent(ctx, SearchLocationActivity.class);
//                locationIntent.putExtra("cityName", activity.application.getCity());
//                startActivityForResult(locationIntent, ACTIVITY_LOCATION);
//                break;
            case R.id.title:
                Intent locationIntent = new Intent(ctx, SearchLocationActivity.class);
                locationIntent.putExtra("cityName", activity.application.getCity());
                startActivityForResult(locationIntent, ACTIVITY_LOCATION);
                break;
        }
    }

    /**
     * SwipeRefreshLayout刷新监听
     */
    @Override
    public void onRefresh() {
//        handler.sendEmptyMessageDelayed(1000, 2000);
//        LatLng latLng = activity.application.getLatLng();
        imagesADRequest();
        getNearbyShop(latitude, longitude);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1000:
                    swipeRefLayout.setRefreshing(false);
                    LogUtils.e("刷新停止");
                    break;
                case 10002:
                    break;
                case NS_CALLBACK_DEFEAT:
                    dismissDialog();
                    break;
                case NS_CALLBACK_SUCCESS:
                    LogUtils.e("附近商家请求成功");
                    NearbyShopBean data = (NearbyShopBean) msg.obj;
                    if (data.getData().size() == 0) {
                        rlNoData.setVisibility(View.VISIBLE);
                        Toast.makeText(ctx, "附近暂无商家", Toast.LENGTH_SHORT).show();
                    } else {
                        rlNoData.setVisibility(View.GONE);
                        Toast.makeText(ctx, "附近商家请求成功", Toast.LENGTH_SHORT).show();

                    }
                    shopEntityList.clear();
                    shopEntityList.addAll(data.getData());
                    mAdapter.notifyDataSetChanged();
                    dismissDialog();
                    break;
                case ImagesRequest_OK:
                    AdListBean adListData = (AdListBean) msg.obj;
                    // 创建一个list存放imageURl
                    List<String> imageList = new ArrayList<>();
                    for (AdListBean.DataEntity dataEntity : adListData.getData()) {
//                        imageList.add(dataEntity.getAD_ImgUrl());
                    }
                    imageList.add("http://img10.360buyimg.com/cms/jfs/t3163/17/1319630042/109911/4d61d2e0/57c94d2cN0928fc96.jpg");
                    imageList.add("http://img11.360buyimg.com/cms/jfs/t3304/208/1257512630/85008/8158e0f/57c94d41Nb1910bf7.jpg");
                    imageList.add("http://img11.360buyimg.com/cms/jfs/t3019/227/1696083172/101137/8f0d5551/57c94d5cN315fd854.jpg");
                    imageList.add("http://img10.360buyimg.com/cms/jfs/t3301/335/1291165781/347071/c6d47d5c/57c94d72N0080bb9a.jpg");

                    initListHead(imageList);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.outLog("onDestroy");
        EventBus.getDefault().unregister(this);
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }

    /**
     * 自定义PullToRefresh的刷新监听类
     */
    public class MyPullToRefreshListener implements PullToRefreshBase.OnRefreshListener2 {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            Toast.makeText(ctx, "刷新", Toast.LENGTH_SHORT).show();
            mainPullList.setRefreshing(false);
        }
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

                EventBus.getDefault().post(location);
                Toast.makeText(ctx, "定位成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, "定位失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_LOCATION:
                LogUtils.e("返回定位当前位置成功");
                if (resultCode == activity.RESULT_OK) {
                    String address = data.getStringExtra(MyKey.BEOBTAINADDRESS);
                    String city = data.getStringExtra(MyKey.POICITY);//兴趣点的名字,不是城市
                    userLocationAdd.setText(city);
                    //获得经纬度进行附近商家列表请求
                    latitude = data.getDoubleExtra("latitude", latitude);
                    longitude = data.getDoubleExtra("longitude", longitude);
                    activity.application.setLatLng(new LatLng(latitude, longitude));
                    getNearbyShop(latitude, longitude);
                } else if (resultCode == SearchLocationActivity.LOCATION_CORRENT) {
                    EventBus.getDefault().post(location);
                    LogUtils.e("TAG","定位成功接收");

                } else if (resultCode == activity.RESULT_CANCELED) {

                }

        }
    }
}
