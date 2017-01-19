package whcs.wohui.zz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import whcs.wohui.zz.adapter.MyBaseAdapter;
import whcs.wohui.zz.service.LocationService;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyKey;
import whcs.wohui.zz.whcouldsupermarket.MyApplication;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/11/5 10:50
 * 版本：V1.0
 * 修改历史：
 */
public class AddUserLocationActivity extends BaseActivity {
    private static final String TAG = "AddUserLocationActivity";
    private MapView bmapView;
    private ListView lvAddress;
    private LinearLayout llTitleMain;
    private RelativeLayout titleGoBack;
    private LinearLayout titleLLName;
    private TextView titleName;
    private EditText titleEdit;
    private RelativeLayout titleDoSearch;
    private ImageView ivTitleSearch;
    private ImageView ivTitleMore;
    private ImageView ivCenterMarker;

    private void assignViews() {
        llTitleMain = (LinearLayout) findViewById(R.id.llTitleMain);
        titleGoBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleLLName = (LinearLayout) findViewById(R.id.titleLLName);
        titleName = (TextView) findViewById(R.id.titleName);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        titleDoSearch = (RelativeLayout) findViewById(R.id.titleDoSearch);
        ivTitleSearch = (ImageView) findViewById(R.id.ivTitleSearch);
        ivTitleMore = (ImageView) findViewById(R.id.ivTitleMore);
        bmapView = (MapView) findViewById(R.id.bmapView);
        lvAddress = (ListView) findViewById(R.id.lv_address);
        ivCenterMarker = (ImageView) findViewById(R.id.iv_center_marker);
    }

    private BaiduMap baiduMap;
    // 定位相关声明
    private LocationService locService;
    private LocationClient locationClient = null;
    private MySearchAddAdapter mySearchAddAdapter;
    private PoiSearch myPoiSearch;
    private GeoCoder geoCoder;
    private LatLng mapCenter;
    List<PoiInfo> poiList;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_user_location);
        assignViews();
        initData();
        initView();
        initTitle();
        initListener();
    }

    /**
     * 初始化标题框
     */
    private void initTitle() {
        findViewById(R.id.titleName).setVisibility(View.GONE);
        titleDoSearch = (RelativeLayout) findViewById(R.id.titleDoSearch);
        titleDoSearch.setVisibility(View.VISIBLE);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setHint("输入地址");
    }

    private void initData() {


        poiList = new ArrayList<>();
        mySearchAddAdapter = new MySearchAddAdapter(poiList);
        lvAddress.setAdapter(mySearchAddAdapter);
        geoCoder = GeoCoder.newInstance();
        //第一步，创建POI检索实例
        myPoiSearch = PoiSearch.newInstance();
        //第2步，设置POI检索监听者；
        myPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        baiduMap = bmapView.getMap();

        //开启定位图层
        baiduMap.setMyLocationEnabled(true);
        locService = ((MyApplication) getApplication()).locationService;
        locService.registerListener(myListener);
        locService.start();
    }

    private void initView() {

    }

    private void initListener() {
        titleGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserLocationActivity.this.onBackPressed();
                finish();
            }
        });
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = (PoiInfo) mySearchAddAdapter.getItem(position);
                jump2HomeFragment(poiInfo);
            }
        });
        geoCoder.setOnGetGeoCodeResultListener(coderResultListener);
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                //创建动画
                Animation shake = AnimationUtils.loadAnimation(AddUserLocationActivity.this, R.anim.shake_y);
                shake.reset();
                shake.setFillAfter(true);
                ivCenterMarker.startAnimation(shake);
                mapCenter = mapStatus.target;
                startSearch(mapStatus.target);
            }
        });
        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                startSearch(mapCenter);
            }
        });
    }


    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || bmapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);    //设置定位数据
            mapCenter = new LatLng(location.getLatitude(),
                    location.getLongitude());
            startSearch(mapCenter);//开始搜索附近poi

            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mapCenter, 19);   //设置地图中心点以及缩放级别
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(u);


        }
    };

    /**
     * 跳转到主页
     *
     * @param poiInfo 检索到的地址信息
     */
    private void jump2HomeFragment(PoiInfo poiInfo) {
        Intent intent = getIntent();
        intent.putExtra(MyKey.LATITUDE, poiInfo.location.latitude);
        intent.putExtra(MyKey.LONGITUDE, poiInfo.location.longitude);
        intent.putExtra(MyKey.BEOBTAINADDRESS, poiInfo.address);
        intent.putExtra(MyKey.POI_NAME, poiInfo.name);
        setResult(RESULT_OK, intent);
        finish();
    }

    //通过地址反编码,得到附近poi
    private void startSearch(LatLng ll) {
        String keyWord = titleEdit.getText().toString().trim();
        LogUtils.e(TAG, "keyWord:" + keyWord + "ll" + ll.toString());
        if (keyWord.equals("")) {
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        } else {
            myPoiSearch.searchNearby(new PoiNearbySearchOption()
                    .keyword(keyWord)
                    .location(ll)
                    .radius(1000));
        }

    }

    //创建geoCoder 监听者
    OnGetGeoCoderResultListener coderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            setPoiData(reverseGeoCodeResult.getPoiList());
        }
    };
    //第二步，创建POI检索监听者；
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            LogUtils.e("333333333333333++++++++++++++++++++++++++" + result.error);
            //获取POI检索结果
            if (result == null
                    || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                LogUtils.outLog("zhaobudao");
                LogUtils.e("111111111111111++++++++++++++++++++++++++");
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                LogUtils.outLog(result.getAllPoi().size() + "");
                List<PoiInfo> list = result.getAllPoi();
                setPoiData(list);
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                LogUtils.e("44444444444444444++++++++++++++++++++++++++");
                String strInfo = "在";
                for (CityInfo cityInfo : result.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(AddUserLocationActivity.this, strInfo, Toast.LENGTH_SHORT).show();
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }
    };

    //请求到的数据,添加到adapter展示
    private void setPoiData(List<PoiInfo> dataList) {
        if (dataList!=null&&dataList.size()>0){
            poiList.clear();
            poiList.addAll(dataList);
            mySearchAddAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 搜索出的列表适配器
     */
    private class MySearchAddAdapter extends MyBaseAdapter<PoiInfo> {
        public MySearchAddAdapter(List<PoiInfo> dataList) {
            super(dataList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(AddUserLocationActivity.this, R.layout.item_search_address_list, null);
                vh.tvPoiName = (TextView) convertView.findViewById(R.id.poiName);
                vh.tvPoiAddress = (TextView) convertView.findViewById(R.id.poiAddress);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tvPoiName.setText(dataList.get(position).name);
            vh.tvPoiAddress.setText(dataList.get(position).address);
            return convertView;
        }

        /**
         * 复用
         */
        private class ViewHolder {
            public TextView tvPoiName;
            public TextView tvPoiAddress;
        }
    }


    // 三个状态实现地图生命周期管理
    @Override
    protected void onDestroy() {
        locService.stop();
        locService.unregisterListener(myListener);
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        bmapView.onDestroy();
        bmapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

}
