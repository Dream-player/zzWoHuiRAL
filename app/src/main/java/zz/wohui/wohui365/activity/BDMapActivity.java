package zz.wohui.wohui365.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.NearbyShopBean;
import zz.wohui.wohui365.callback.NearbyShopCallBack;
import zz.wohui.wohui365.service.LocationService;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.MyApplication;
import zz.wohui.wohui365.R;

/**
 * 作者：陈杰宇
 * 时间： 2016/1/5 12:43
 */
public class BDMapActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BDMapActivity";
    BaiduMap mBaiduMap;
    public LocationClient mLocationClient = null;
    private LocationService locService;

    private MapView mMapView;//地图控件
    private RelativeLayout btnLocation;//定位按钮
    private RelativeLayout relBottomHalf;//增大缩放等级
    private RelativeLayout relTopHalf;//减小缩放等级
    private float mapZoomLevel = 16;//地图缩放等级
    private static final int NS_CALLBACK_SUCCESS = 10003;//附近商家请求成功
    private static final int NS_CALLBACK_DEFEAT = 10004;//附近商家请求失败
    private static final int DO_LOCATION_SUCCESS = 45661;//定位成功
    private MyRequestParams params;
    private MyOkHttpUtils myOkHttpUtils;

    private void assignViews() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        btnLocation = (RelativeLayout) findViewById(R.id.rel_bdmap_location);
        relBottomHalf = (RelativeLayout) findViewById(R.id.rel_bottom_half);
        relTopHalf = (RelativeLayout) findViewById(R.id.rel_top_half);
    }

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
//        locService = ((MyApplication) getApplication()).locationService;
        setContentView(R.layout.activity_bdmap);
        assignViews();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();//得到地图实体类
//        doLocation();
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                mBaiduMap.hideInfoWindow();
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                mapZoomLevel = mapStatus.zoom;
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
        relBottomHalf.setOnClickListener(this);
        relTopHalf.setOnClickListener(this);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 34.823854);
        double longitude = intent.getDoubleExtra("longitude", 113.571824);
        moveTOPoint(latitude, longitude);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                Bundle bundle = marker.getExtraInfo();
                String shopNumber = bundle.getString("shopNumber");
                if (title != null) {
                    Button button = new Button(BDMapActivity.this);
                    button.setBackgroundResource(R.drawable.bg_map_popup);
                    button.setText(title);
                    LatLng pt = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
                    //显示InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    button.setOnClickListener(new MyOnClickListener(shopNumber));
                } else {
                    Toast.makeText(BDMapActivity.this, "未知位置", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaiduMap != null) {
                    mBaiduMap.clear();
                }
                doLocation();

            }
        });
    }

    /**
     * 进行定位
     */
    private void doLocation() {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mapZoomLevel = 16;
        locService = ((MyApplication) getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_bottom_half:

                if (mapZoomLevel > 3) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                    mapZoomLevel--;
                } else {
                    Toast.makeText(this, "地图已缩放至最小", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rel_top_half:
                if (mapZoomLevel < 20) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                    mapZoomLevel++;
                } else {
                    Toast.makeText(this, "地图已放大至最大", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                Message locMsg = locHander.obtainMessage();
                Bundle locData = new Bundle();
                locData.putParcelable("loc", location);
                locMsg.setData(locData);
                locMsg.what = DO_LOCATION_SUCCESS;
                locHander.sendMessage(locMsg);
                LogUtils.e(TAG,"定位成功");

            } else {
                Toast.makeText(BDMapActivity.this, "failed to obtain location information,please check your network", Toast.LENGTH_SHORT).show();
            }
        }
    };
    /***
     * 接收定位结果消息，并显示在地图上
     */
    private Handler locHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DO_LOCATION_SUCCESS:
                    try {
                        BDLocation location = msg.getData().getParcelable("loc");
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            showUserLocation(latitude,longitude);//在地图上设置用户小圆点
                            moveTOPoint(latitude, longitude);//地图中心点移动到用户位置
                            getNearbyShop(location.getLatitude(), location.getLongitude());
                        } else {
                            Toast.makeText(BDMapActivity.this, "location failure2", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        Toast.makeText(BDMapActivity.this, "location failure1", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NS_CALLBACK_SUCCESS:
                    NearbyShopBean nearbyShopBean = (NearbyShopBean) msg.obj;
                    showNearbyShop(nearbyShopBean.getData());
                    dismissDialog();
                    break;
            }
            }
    };
    /**
     *把地图的中心点移动到对应经纬度坐标的位置
     * @param latitude 纬度
     * @param longitude 经度
     */
    private void moveTOPoint(double latitude,double longitude){
        LatLng point = new LatLng(latitude,longitude);//地图中心点
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.shop);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 在地图上展示用户当前位置
     * @param latitude
     * @param longitude
     */
    private void showUserLocation(double latitude,double longitude){
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //构建定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(latitude).longitude(longitude).build();
        //设置定位数据
        mBaiduMap.setMyLocationData(locData);
    }

    /**
     * 在地图上显示周边超市
     * @param shopList 周边超市实体类列表
     */
    private void showNearbyShop(List<NearbyShopBean.DataEntity> shopList){
        LogUtils.e(TAG,"在地图上显示周边超市");
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.shop);
        for (NearbyShopBean.DataEntity shopData : shopList){
            //定义Maker坐标点
            LatLng point = new LatLng(shopData.getS_Lat(),shopData.getS_Long());
            //构建MarkerOption，用于在地图上添加Marker
            Bundle bundle = new Bundle();
            bundle.putString("shopNumber",shopData.getS_GUID());
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .title(shopData.getS_Name())
                    .extraInfo(bundle);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            locService.unregisterListener(listener);
            locService.stop();
        }
    }
    /**
     * 自定义监听函数
     */
    private class MyOnClickListener implements View.OnClickListener {

        private String json;

        public MyOnClickListener(String json) {
            this.json = json;
        }

        @Override
        public void onClick(View v) {
            jump2Shop(json);
        }
    }

    /**
     * 跳转到商家
     *
     * @param json 商家信息
     */
    private void jump2Shop(String json) {

        Intent intent = new Intent(this,ShopActivity.class);
        intent.putExtra("serialNumber",json);
        startActivity(intent);
        BDMapActivity.this.finish();
    }

//    /**
//     * 根据经纬度获取附近的商铺并显示出来
//     *
//     * @param latitude  当前的经度
//     * @param longitude 当前的纬度
//     */
//    private void getNearbyShop(double latitude, double longitude) {
//        LogUtils.outLog("获取经纬度");
//        // 构建Marker图标
//        BitmapDescriptor bitmap = null;
//        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bdmap_user_loaction);
//        LatLng point = null;
//        double[][] testNum = {{34.826574, 113.569846}, {34.825322, 113.568809}, {34.825129, 113.570525}, {34.825163, 113.567695}, {34.826759, 113.570147}, {113.571935, 34.823474}, {34.823959, 113.575093}};
//        String[] shopName = {"no Name", "万州烤鱼", "君荷酒店", "湘香烤鱼", "圣禧牛肉汤", "中国工商银行", "莲花公园"};
//        Bundle bundle = new Bundle();
//        for (int i = 0; i < testNum.length; i++) {
//            bundle.clear();
//            bundle.putString("shopName", shopName[i]);
//            point = new LatLng(testNum[i][0], testNum[i][1]);
//            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap).title(shopName[i]);
//            mBaiduMap.addOverlay(option);
//        }
//        Toast.makeText(this, "结束弹窗", Toast.LENGTH_SHORT).show();
//    }
    /**
     * 网络请求获得附近商家
     * @param latitude 纬度
     * @param longitude 经度
     */
    private void getNearbyShop(double latitude,double longitude){
        LogUtils.e(TAG,"网络请求附近超市");
        showDialog(this);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, this.getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.NearbyShop_Latitude,latitude+"");
        params.addStringRequest(ParamsKey.NearbyShop_Longitude,longitude+"");
        params.addStringRequest(ParamsKey.NearbyShop_OrderKey,"ScoreSM-DESC");//默认距离排序
        params.addStringRequest(ParamsKey.NearbyShop_Distance,"100000");//默认范围
        String strUrl = Urls.GetNearbyShop;
        myOkHttpUtils.postRequest(strUrl, params, new NearbyShopCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(BDMapActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                locHander.sendEmptyMessage(NS_CALLBACK_DEFEAT);
            }

            @Override
            public void onResponse(NearbyShopBean response) {
                if (response.getState() == 1) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = NS_CALLBACK_SUCCESS;
                    locHander.sendMessage(msg);

                } else {
                    Toast.makeText(BDMapActivity.this, "返回网络状态" + response.getState(), Toast.LENGTH_SHORT).show();
                    dismissDialog();
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        locService.unregisterListener(listener);
//        locService.stop();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}