package zz.wohui.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import zz.wohui.zz.adapter.MyBaseAdapter;
import zz.wohui.zz.service.LocationService;
import zz.wohui.zz.utils.LogUtils;
import zz.wohui.zz.utils.MyKey;
import zz.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：输入地址 自定义定位的Activity
 * 作者：陈杰宇
 * 时间： 2016/1/26 11:30
 * 版本：V1.0
 * 修改历史：
 */
public class SearchLocationActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llLocation;
    private TextView tvLocation;
    private View titleGoBack;
    private ImageView ivLocationIC;
    private ProgressBar addressSuggestionLocating;
    private ListView lvAddressList;
    String cityName;
    private Intent intent;

    private void assignViews() {
        llLocation = (LinearLayout) findViewById(R.id.llLocation);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        ivLocationIC = (ImageView) findViewById(R.id.ivLocationIC);
        addressSuggestionLocating = (ProgressBar) findViewById(R.id.address_suggestion_locating);
        lvAddressList = (ListView) findViewById(R.id.lvAddressList);
        titleGoBack = findViewById(R.id.titleGoBack);
    }

    private EditText titleEdit;
    private RelativeLayout doSearch;
    private PoiSearch myPoiSearch;
    private Context ctx;
    private MySearchAddAdapter mySearchAddAdapter;
    List<PoiInfo> poiList;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_location);
        assignViews();
        initTitle();
        ctx = this;
        initData();
        initPoi();
        llLocation.setOnClickListener(this);
        lvAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiInfo poiInfo = (PoiInfo) mySearchAddAdapter.getItem(position);
                jump2HomeFragment(poiInfo);
            }
        });
    }
    /**
     *初始换ListView
     */
    private void initData(){
        poiList=new ArrayList<>();
        mySearchAddAdapter = new MySearchAddAdapter(poiList);
        lvAddressList.setAdapter(mySearchAddAdapter);
    }
    /**
     * 跳转到主页
     *
     * @param poiInfo 检索到的地址信息
     */
    private void jump2HomeFragment(PoiInfo poiInfo) {
        intent.putExtra(MyKey.WHEREFROM, "SearchLocationActivity");
        intent.putExtra("latitude", poiInfo.location.latitude);
        intent.putExtra("longitude", poiInfo.location.longitude);
        intent.putExtra(MyKey.BEOBTAINADDRESS, poiInfo.address);
        intent.putExtra(MyKey.POICITY, poiInfo.name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            LogUtils.e("onKeyDown==KeyEvent.KEYCODE_BACK");
            setResult(RESULT_CANCELED,intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化地址检索
     */
    private void initPoi() {
        intent = getIntent();
        cityName = intent.getStringExtra("cityName");
        LogUtils.e("poi搜索的城市:"+cityName);
//        cityName = intent.getStringExtra("cityName");
//        if (cityName == null || cityName == "") {
//            cityName = "郑州";
//        }
        //第一步，创建POI检索实例
        myPoiSearch = PoiSearch.newInstance();
        //第二步，创建POI检索监听者；
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                LogUtils.e("333333333333333++++++++++++++++++++++++++"+result.error);
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
                    LogUtils.outLog(list.size() + "");
                    for (PoiInfo poiInfo : list) {
                        LogUtils.outLog(poiInfo.name + poiInfo.address);
                    }
                    poiList.clear();
                    poiList.addAll(list);
                    LogUtils.e("22222222222222++++++++++++++++++++++++++");
                             mySearchAddAdapter.notifyDataSetChanged();
//                    fillAddressList(list);
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
                    Toast.makeText(SearchLocationActivity.this, strInfo, Toast.LENGTH_SHORT).show();
                }
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }
        };
        //第三步，设置POI检索监听者；
        myPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearchPoi(titleEdit.getText().toString());
                LogUtils.e("****************++++++++++++++++++++++++++" + titleEdit.getText().toString().trim());

            }
        });
//        titleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                doSearchPoi(titleEdit.getText().toString().trim());
//                LogUtils.e("**********************************+++++++++++++++++++++++++++++++++++++");
//                return false;
//            }
//        });
    }

    /**
     * 初始化标题框
     */
    private void initTitle() {
        findViewById(R.id.titleName).setVisibility(View.GONE);
        doSearch = (RelativeLayout) findViewById(R.id.titleDoSearch);
        doSearch.setVisibility(View.GONE);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setHint("输入地址");
        titleEdit.setOnClickListener(this);
        doSearch.setOnClickListener(this);
        titleGoBack.setOnClickListener(this);
    }

    /**
     * 百度地图检索监听类
     */


    /**
     * 完成Address填充
     *
     * @param list 数据列表
     */
    private void fillAddressList(List<PoiInfo> list) {

        mySearchAddAdapter = new MySearchAddAdapter(list);
        lvAddressList.setAdapter(mySearchAddAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleEdit:
                llLocation.setVisibility(View.GONE);
                lvAddressList.setVisibility(View.VISIBLE);
                break;
            case R.id.titleDoSearch:
                String address = titleEdit.getText().toString().trim();
                if (address.equals("")) {
                    Toast.makeText(ctx, "请输入地址", Toast.LENGTH_SHORT).show();
                    break;
                }
                doSearchPoi(address);
                break;
            case R.id.llLocation:
//                doLocation();
                setResult(LOCATION_CORRENT);
                SearchLocationActivity.this.finish();
                Toast.makeText(ctx, "定位成功", Toast.LENGTH_SHORT).show();

                break;

            case R.id.titleGoBack:
                onBackPressed();
                break;
        }
    }

    /**
     * 进行地址检索
     *
     * @param address 地址
     */
    private void doSearchPoi(String address) {
        myPoiSearch.searchInCity((new PoiCitySearchOption()
                .city(cityName)
                .keyword(address)));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myPoiSearch.destroy();
    }
    private BDLocation location;
    private LocationService locService;
    public static final int LOCATION_CORRENT = 44;
    /***
     * 定位结果回调，在此方法中处理定位结果
     */
//    BDLocationListener listener = new BDLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location1) {
//
//            if (location1 != null && (location1.getLocType() == 161 || location1.getLocType() == 66)) {
//                location = location1;
//                Toast.makeText(ctx, "定位成功", Toast.LENGTH_SHORT).show();
//                intent.putExtra("location", location1);
//                setResult(LOCATION_CORRENT, intent);
//                dismissDialog();
//                SearchLocationActivity.this.finish();
//            } else {
//                Toast.makeText(ctx, "定位失败", Toast.LENGTH_SHORT).show();
//                dismissDialog();
//            }
//        }
//    };
    /**
     * 进行定位
     */
//    private void doLocation() {
//        showDialog(ctx);
//        LogUtils.outLog("doLocation()");
//        locService = getMyApplication().locationService;
//        LocationClientOption mOption = locService.getDefaultLocationClientOption();
//        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//        mOption.setCoorType("bd09ll");
//        locService.setLocationOption(mOption);
//        locService.registerListener(listener);
//        locService.start();
//    }
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
                convertView = View.inflate(SearchLocationActivity.this.ctx, R.layout.item_search_address_list, null);
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
    }

    /**
     * 复用
     */
    private class ViewHolder {
        public TextView tvPoiName;
        public TextView tvPoiAddress;
    }
}
