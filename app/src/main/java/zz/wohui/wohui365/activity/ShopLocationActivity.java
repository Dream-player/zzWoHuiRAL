package zz.wohui.wohui365.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import zz.wohui.wohui365.R;



/**
 * 说明：商家地图所在位置页面
 * 作者：陈杰宇
 * 时间： 2016/2/20 10:14
 * 版本：V1.0
 * 修改历史：
 */
public class ShopLocationActivity extends BaseActivity {

    private MapView bdMapView;
    private TextView titleName;
    private RelativeLayout goBack;

    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        bdMapView = (MapView) findViewById(R.id.bdMapView);
    }

    private TextView tvShopName;
    private TextView tvDistance;
    private TextView tvAddress;
    private Context ctx;
    private BaiduMap mBaiduMap;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shop_location);
        assignViews();
        ctx = this;
        mBaiduMap = bdMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        // TODO: 2016/2/20 根据信息显示商家位置图标
        titleName.setText("商家名称");
        showShopMark();
    }

    private void showShopMark() {
        BitmapDescriptor bitmapMark =
                BitmapDescriptorFactory.fromResource(R.drawable.icon_poi_position);
        LatLng point = new LatLng(34.826574, 113.569846);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmapMark).title("丹尼斯—翠竹街店");
        mBaiduMap.addOverlay(option);
        View view = View.inflate(ctx,R.layout.poi_shop_location,null);
        assignViews(view);
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(view, point, -60);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
    }

    private void assignViews(View view) {
        tvShopName = (TextView) view.findViewById(R.id.tvShopName);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
    }

}
