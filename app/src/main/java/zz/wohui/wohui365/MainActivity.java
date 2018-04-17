package zz.wohui.wohui365;

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

import zz.wohui.wohui365.activity.BaseActivity;
import zz.wohui.wohui365.fragment.BlankFragment1;
import zz.wohui.wohui365.fragment.BlankFragment2;
import zz.wohui.wohui365.fragment.BlankFragment3;
import zz.wohui.wohui365.fragment.BlankFragment4;
import zz.wohui.wohui365.myview.MyFragmentTabHost;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyKey;

public class MainActivity extends BaseActivity {

    private BDLocation location;

    public BDLocation getmLocation() {
        return location;
    }

    public void setmLocation(BDLocation location) {
        this.location = location;
    }

    private String texts[] = {"商城", "分类", "发现", "我的"};//"购物车"
    private Class fragmentArray[] = {
            BlankFragment1.class,
            BlankFragment2.class,
            BlankFragment3.class,
            BlankFragment4.class};
//            FirstFragment.class,
//            HomeFragment.class,
//            OrderFragment.class,
//            UserCenterFragment.class};
    private long fastTime = 0;

    private int imageBG[] = {
            zz.wohui.wohui365.R.drawable.tab_content_image_selector1,
            zz.wohui.wohui365.R.drawable.tab_content_image_selector4,
//            R.drawable.tab_content_image_selector5,
            zz.wohui.wohui365.R.drawable.tab_content_image_selector2,
            zz.wohui.wohui365.R.drawable.tab_content_image_selector3};
    private long secondTime;

    private FrameLayout mainContent;
    private MyFragmentTabHost tabHost;
    private FrameLayout tabContent;


    private void assignViews() {
        mainContent =  findViewById(R.id.mainContent);
        tabHost = (MyFragmentTabHost) findViewById(zz.wohui.wohui365.R.id.tabHost);
        tabContent = (FrameLayout) findViewById(zz.wohui.wohui365.R.id.tabContent);
    }

    private Context ctx;
    private String whereFrom = "";

    public MyApplication application;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(zz.wohui.wohui365.R.layout.activity_main);
        ctx = MainActivity.this;

        assignViews();
        tabHost.setup(ctx, getSupportFragmentManager(), zz.wohui.wohui365.R.id.mainContent);
        for (int i = 0; i < texts.length; i++) {
            TabHost.TabSpec spec = tabHost.newTabSpec(texts[i]).setIndicator(getView(i));
            tabHost.addTab(spec, fragmentArray[i], null);
            tabHost.getTabWidget().setDividerDrawable(null);
        }
        application = (MyApplication) getApplication();
        Intent intent = getIntent();
        whereFrom = intent.getStringExtra(MyKey.WHEREFROM);
        if (whereFrom != null && whereFrom != "") {
            switch (whereFrom) {
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
        View view = View.inflate(ctx, zz.wohui.wohui365.R.layout.tabcontent, null);
        ImageView tabContentImage = (ImageView) view.findViewById(zz.wohui.wohui365.R.id.tabContentImage);
        TextView tabContentName = (TextView) view.findViewById(zz.wohui.wohui365.R.id.tabContentName);
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
