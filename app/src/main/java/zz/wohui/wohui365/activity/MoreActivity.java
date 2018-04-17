package zz.wohui.wohui365.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.updateApp.todo.UpdateChecker;
import zz.wohui.wohui365.utils.updateApp.util.CommonUtil;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：更多页面—版本号 检查更新 关于我惠等
 * 作者：陈杰宇
 * 时间： 2016/2/18 14:01
 * 版本：V1.0
 * 修改历史：
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivSupermarketIcon;
    private TextView tvVersionNum;
    private TextView tvWoHui365;
    private TextView tvAboutWoHui;
    private TextView moreLine;
    private TextView tvCheckUpdate;
    private RelativeLayout goBack;
    private TextView titleName;
    private int versionCodeClient;
    private UpdateChecker updateChecker;
    private void assignViews() {
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleName = (TextView) findViewById(R.id.titleName);
        ivSupermarketIcon = (ImageView) findViewById(R.id.ivSupermarketIcon);
        tvVersionNum = (TextView) findViewById(R.id.tvVersionNum);
        tvWoHui365 = (TextView) findViewById(R.id.tvWoHui365);
        tvAboutWoHui = (TextView) findViewById(R.id.tvAboutWoHui);
        moreLine = (TextView) findViewById(R.id.moreLine);
        tvCheckUpdate = (TextView) findViewById(R.id.tvCheckUpdate);
    }


    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_more);
        assignViews();
        titleName.setText("更多");
        goBack.setOnClickListener(this);
        tvAboutWoHui.setOnClickListener(this);
        tvCheckUpdate.setOnClickListener(this);
        getVersionNum();
    }

    /**
     * 获取并显示版本号
     */
    private void getVersionNum() {
        String number = "1.0";
        int versionCode = 1;
        try {
            number = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
            versionCode = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e1) {

            number = "1.0";

            e1.printStackTrace();
        }
        tvVersionNum.setText("v" + number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                MoreActivity.this.onBackPressed();
                finish();
                break;
            case R.id.tvCheckUpdate:
                checkUpdate();
                break;
            case R.id.tvAboutWoHui:
                Intent intent = new Intent(this,WHInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UpdateChecker.REQ_WIRELESS_SETTINGS:
                updateChecker.checkVersion(Urls.UpdateApp,CommonUtil.getVersionName(this));
                break;
        }
    }
    /**
     * 检查更新
     */
    private void checkUpdate() {
        LogUtils.e("检查更新");
        versionCodeClient = CommonUtil.getVersionCodeClient(this);
        updateChecker = new UpdateChecker(this, versionCodeClient);
        updateChecker.checkVersion(Urls.UpdateApp,CommonUtil.getVersionName(this));
    }
}
