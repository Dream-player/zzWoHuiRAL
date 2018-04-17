package zz.wohui.wohui365.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.LoginBean;
import zz.wohui.wohui365.callback.LoginCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.MainActivity;
import zz.wohui.wohui365.R;

/**
 * 说明：登录页面
 * 作者：陈杰宇
 * 时间： 2016/1/23 13:54
 * 版本：V1.0
 * 修改历史：
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText etCardNumber;
    private EditText etPassword;
    private Button btnSignIN;
    private TextView titleName;
    private RelativeLayout goBack;
    private TextView tvRecoverPwd;
    private SharedPreferences sp;

    private void assignViews() {
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIN = (Button) findViewById(R.id.btnSignIN);
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        tvRecoverPwd = (TextView) findViewById(R.id.tvRecoverPwd);
    }

    private Context ctx;
    private String strCardNum;
    private String strPwd;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ctx = this;
        assignViews();
        titleName.setText("登录");
        goBack.setOnClickListener(this);
        btnSignIN.setOnClickListener(this);
        tvRecoverPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIN:
                //TODO 登录
                strCardNum = etCardNumber.getText().toString().trim();
                strPwd = etPassword.getText().toString().trim();
                if (strCardNum == null || strCardNum.equals("")) {
                    etCardNumber.setError("用户名不能为空");
                    break;
                }
                if (strPwd == null || strPwd.equals("")) {
                    etPassword.setError("密码不能为空");
                    break;
                }
                doSignIN();
                break;
            case R.id.titleGoBack:
                backMain(false);
                break;
            case R.id.tvRecoverPwd:
                //TODO 找回密码
                break;
        }
    }

    /**
     * 返回首页
     *
     * @param isLogin
     */
    private void backMain(boolean isLogin) {
//        this.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 进行登录操作
     */
    private void doSignIN() {
        showDialog(ctx);
        MyRequestParams requestParams = new MyRequestParams();
        requestParams.clear();
        requestParams.addStringRequest(ParamsKey.Login_Name, strCardNum);
        requestParams.addStringRequest(ParamsKey.Login_Pwd, strPwd);
        MyOkHttpUtils myOkHttpUtils = new MyOkHttpUtils(this);
        myOkHttpUtils.postRequest(Urls.Login, requestParams, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                showToast(ctx, "登录失败,请检查网络");
                LogUtils.e(TAG,e.toString());
            }

            @Override
            public void onResponse(LoginBean response) {
                dismissDialog();
                if (response.getState() == 1) {
                    saveUserData(response.getData().getUGUID(),response.getData().getUNumber());
                    backMain(true);
                } else {
                    showToast(ctx, "登录失败,请检查账户名密码是否正确");
                }
            }


        });
    }

    /**
     * 存储用户数据
     */
    private void saveUserData(String data,String cardNum) {
        SharedPreferences sp = getSharedPreferences(MyKey.userConfig, MODE_PRIVATE);
        sp.edit().putString("userKey", data)
                .putString("userCard", strCardNum)
                .putString("cardNumber",cardNum)
                .commit();
        getMyApplication().setUserKey(data);
    }
}
