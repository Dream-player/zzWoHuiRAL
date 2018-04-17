package zz.wohui.wohui365.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.UserInfoBean;
import zz.wohui.wohui365.callback.UserInfoCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.MainActivity;
import zz.wohui.wohui365.R;

/**
 * 说明：基本资料页面
 * 作者：陈杰宇
 * 时间： 2016/2/18 15:26
 * 版本：V1.0
 * 修改历史：
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivUserHeadAvatar;
    private TextView tvChangeLoginPwd;
    private TextView tvChangePayPwd;
    private RelativeLayout goBack;
//    private TextView tvCanregCount;
//    private TextView tvConsumeBonus;
    private TextView tvRealName;
    private TextView tvPhoneNum;
    private TextView tvAddress;
    private TextView titleName;
    private TextView tvLogOut;

    private void assignViews() {
        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleName = (TextView) findViewById(R.id.titleName);
        ivUserHeadAvatar = (ImageView) findViewById(R.id.ivUserHeadAvatar);
        tvChangeLoginPwd = (TextView) findViewById(R.id.tvChangeLoginPwd);
        tvChangePayPwd = (TextView) findViewById(R.id.tvChangePayPwd);
        tvLogOut = (TextView) findViewById(R.id.tvLogout);
//        tvCanregCount = (TextView) findViewById(tvCanregCount);
//        tvConsumeBonus = (TextView) findViewById(tvConsumeBonus);
    }

    private Context ctx;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_info);
        assignViews();
        ctx = this;
        titleName.setText("基本资料");
        goBack.setOnClickListener(this);
        tvChangeLoginPwd.setOnClickListener(this);
        tvChangePayPwd.setOnClickListener(this);
        tvLogOut.setOnClickListener(this);
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
                UserInfoActivity.this.onBackPressed();
                finish();
            }
            @Override
            public void onResponse(UserInfoBean response) {
                dismissDialog();
                if (response.getState() == 1) {
                    initView(response.getData().getData().get(0));
                } else {
                    Toast.makeText(ctx, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    UserInfoActivity.this.onBackPressed();
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
        tvRealName.setText(dataEntity.getC_RealName());
        tvPhoneNum.setText(dataEntity.getC_Phone());
        tvAddress.setText(dataEntity.getC_Province()
                + dataEntity.getC_City() +
                dataEntity.getC_District());
//        tvCanregCount.setText(dataEntity.getC_CanRegCount());
//        tvConsumeBonus.setText(dataEntity.getC_Consume_Bonus());
    }

    @Override
    public void onClick(View v) {

        final Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.titleGoBack:
                UserInfoActivity.this.onBackPressed();
                finish();
                break;
            case R.id.tvChangeLoginPwd:
                intent.setClass(ctx, ChangePwdActivity.class);
                intent.putExtra("isChangePayPwd", false);
                startActivity(intent);
                break;
            case R.id.tvChangePayPwd:
                intent.setClass(ctx, ChangePwdActivity.class);
                intent.putExtra("isChangePayPwd", true);
                startActivity(intent);
                break;
            case R.id.tvLogout:
                MyUtils.showAlertDialog(ctx, "提示", "确定退出登录么？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences(MyKey.userConfig, MODE_PRIVATE);
                        sp.edit().putString("userKey", "")
                                .putString("userCard", "")
                                .commit();
                        getMyApplication().setUserKey("");
                        intent.setClass(ctx, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
        }
    }
}
