package whcs.wohui.zz.whcouldsupermarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

import whcs.wohui.zz.activity.BaseActivity;
import whcs.wohui.zz.fragment.FirstFragment;
import whcs.wohui.zz.fragment.HomeFragment;
import whcs.wohui.zz.fragment.OrderFragment;
import whcs.wohui.zz.fragment.UserCenterFragment;
import whcs.wohui.zz.myview.MyFragmentTabHost;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyKey;

public class MainActivity extends BaseActivity {

    private BDLocation location;

    public BDLocation getmLocation() {
        return location;
    }

    public void setmLocation(BDLocation location) {
        this.location = location;
    }

    private String texts[] = {"首页","附近", "订单", "我的"};//"购物车"
    private Class fragmentArray[] = {FirstFragment.class,
            HomeFragment.class,
//            ShopCarFragment.class,
            OrderFragment.class,
            UserCenterFragment.class};
    private long fastTime = 0;

    private int imageBG[] = {
            R.drawable.tab_content_image_selector1,
            R.drawable.tab_content_image_selector4,
//            R.drawable.tab_content_image_selector5,
            R.drawable.tab_content_image_selector2,
            R.drawable.tab_content_image_selector3};
    private long secondTime;

    private FrameLayout mainContent;
    private MyFragmentTabHost tabHost;
    private FrameLayout tabContent;


    private void assignViews() {
        mainContent = (FrameLayout) findViewById(R.id.mainContent);
        tabHost = (MyFragmentTabHost) findViewById(R.id.tabHost);
        tabContent = (FrameLayout) findViewById(R.id.tabContent);
    }

    private Context ctx;
    private String whereFrom = "";

    public MyApplication application;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ctx = MainActivity.this;

        assignViews();
        tabHost.setup(ctx, getSupportFragmentManager(), R.id.mainContent);
        for (int i = 0; i < texts.length; i++) {
            TabHost.TabSpec spec = tabHost.newTabSpec(texts[i]).setIndicator(getView(i));
            tabHost.addTab(spec, fragmentArray[i], null);
            tabHost.getTabWidget().setDividerDrawable(null);
        }
        application = (MyApplication) getApplication();
        Intent intent = getIntent();
        whereFrom = intent.getStringExtra(MyKey.WHEREFROM);
        if (whereFrom != null && whereFrom != "") {
            switch (whereFrom){
                case "LoginActivity":
                    tabHost.setCurrentTab(2);
                    break;
                case "SearchLocationActivity":
                    LogUtils.e("from:SearchLocationActivity");
                    application.setCity(intent.getStringExtra(MyKey.POICITY));
                    application.setLatLng((LatLng) intent.getParcelableExtra(MyKey.LONGANDLAT));
                    application.setAddress(intent.getStringExtra(MyKey.BEOBTAINADDRESS));
                    break;
            }
        }
    }

    private View getView(int i) {
        View view = View.inflate(ctx, R.layout.tabcontent, null);
        ImageView tabContentImage = (ImageView) view.findViewById(R.id.tabContentImage);
        TextView tabContentName = (TextView) view.findViewById(R.id.tabContentName);
        tabContentImage.setImageResource(imageBG[i]);
        tabContentName.setText(texts[i]);
        return view;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            secondTime = SystemClock.uptimeMillis();
            if (secondTime - fastTime > 1500) {
                Toast.makeText(MainActivity.this, "再按一次退出哦~", Toast.LENGTH_SHORT).show();
                fastTime = secondTime;
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 关闭当前activity
     */
    public void doFinish() {
        MainActivity.this.finish();
    }
}
