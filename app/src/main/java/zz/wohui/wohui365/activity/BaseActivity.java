package zz.wohui.wohui365.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import zz.wohui.wohui365.myview.MyProgressDialog;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.SystemBarTintManager;
import zz.wohui.wohui365.MyApplication;
import zz.wohui.wohui365.R;

/**
 * 说明：项目中Activity的基类
 * 作者：陈杰宇
 * 时间： 2016/1/7 14:47
 * 版本：V1.0
 * 修改历史：
 */
public abstract class BaseActivity extends FragmentActivity {

    public MyApplication getMyApplication() {
        return myApplication;
    }
    View v;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        titleColor();
        myApplication = (MyApplication) getApplication();
        myOnCreate(savedInstanceState);
    }

    public abstract void myOnCreate(Bundle savedInstanceState);

    /**
     * 设定标题栏颜色
     */
    public void titleColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.titleColor);//通知栏所需颜色
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 一般Get方式进行联网
     *
     * @param url      链接
     * @param callback 返回
     */
    public void okHttpGet(String url, Callback callback) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);
    }

    public static MyProgressDialog myProgressDialog;

    /**
     * 显示进度框
     *
     * @param ctx Context
     */
    public void showDialog(Context ctx) {
        myProgressDialog = MyProgressDialog.create(ctx).show();
    }

    /**
     * 关闭弹窗
     */
    public void dismissDialog() {
        myProgressDialog.dismiss();
    }

    /**
     * 弹出toast
     *
     * @param ctx context
     * @param str 内容
     */
    public static void showToast(Context ctx, String str) {
        Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断是否已经登录
     *
     * @return
     */
    public boolean isLogin() {
        SharedPreferences sp = getSharedPreferences(MyKey.userConfig, MODE_PRIVATE);
        return !sp.getString("userKey", "").equals("");
    }
    /**
     * 登录
     */
    public void login(Intent intent) {
        intent.setClass(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
