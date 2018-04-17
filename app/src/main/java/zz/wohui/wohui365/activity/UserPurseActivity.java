package zz.wohui.wohui365.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.UserInfoBean;
import zz.wohui.wohui365.callback.UserInfoCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.R;

/**
 * 说明：基本资料页面
 * 作者：陈杰宇
 * 时间： 2016/2/18 15:26
 * 版本：V1.0
 * 修改历史：
 */
public class UserPurseActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout goBack;
    private TextView tvCanregCount;
    private TextView tvConsumeBonus;
    private TextView titleName;

    private void assignViews() {
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleName = (TextView) findViewById(R.id.titleName);
        tvCanregCount = (TextView) findViewById(R.id.tvCanregCount);
        tvConsumeBonus = (TextView) findViewById(R.id.tvConsumeBonus);
    }

    private Context ctx;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_purse);
        assignViews();
        ctx = this;
        titleName.setText("我的钱包");
        goBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 获取信息
     */
    private void initData() {
        showDialog(ctx);
        MyOkHttpUtils myOkHttpUtils = new MyOkHttpUtils();
        MyRequestParams params = new MyRequestParams();
        params.clear();
        String userKey = getMyApplication().getUserKey();
        if(userKey.equals("")){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        params.addStringRequest(ParamsKey.userKey, userKey);
        myOkHttpUtils.postRequest(Urls.GetUserInfo, params, new UserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(ctx, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
                UserPurseActivity.this.onBackPressed();
                finish();
            }
            @Override
            public void onResponse(UserInfoBean response) {
                dismissDialog();
                if (response.getState() == 1) {
                    initView(response.getData().getData().get(0));
                } else {
                    Toast.makeText(ctx, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    UserPurseActivity.this.onBackPressed();
                    finish();
                }
            }
        });
    }

    /**
     * 填充数据
     *
     * @param dataEntity 获取到的用户信息
     */
    private void initView(UserInfoBean.DataEntity.dataEntity dataEntity) {
        tvCanregCount.setText(dataEntity.getC_CanRegCount());
        tvConsumeBonus.setText(dataEntity.getC_Consume_Bonus());
    }

    @Override
    public void onClick(View v) {

        final Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.titleGoBack:
                UserPurseActivity.this.onBackPressed();
                finish();
                break;
        }
    }
}
