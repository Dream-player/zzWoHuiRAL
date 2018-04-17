package zz.wohui.wohui365.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import zz.wohui.wohui365.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/7 15:29
 * 版本：V1.0
 * 修改历史：
 */
public class ShopMapActivity extends BaseActivity{

    private MapView shopMap;
    private BaiduMap mBaiduMap;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map_shop);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude",34.823854);
        double longitude = intent.getDoubleExtra("longitude",113.571824);
        initMap(latitude,longitude);

    }

    private void initMap(double latitude,double longitude) {
        shopMap = (MapView) findViewById(R.id.map_shop);
        mBaiduMap = shopMap.getMap();
        //定义Maker坐标点
        LatLng point = new LatLng(latitude,longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.shop);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        //设定中心点坐标
        LatLng cenpt =  new LatLng(latitude,longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        shopMap.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        shopMap.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        shopMap.onPause();
    }
}
