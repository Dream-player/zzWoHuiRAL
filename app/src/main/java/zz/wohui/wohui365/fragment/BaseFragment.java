package zz.wohui.wohui365.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shizhefei.fragment.LazyFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import zz.wohui.wohui365.activity.LoginActivity;
import zz.wohui.wohui365.myview.MyProgressDialog;
import zz.wohui.wohui365.utils.MyKey;

/**
 * 说明：Fragment的基类
 * 作者：陈杰宇
 * 时间： 2016/1/8 16:35
 * 版本：V1.0
 * 修改历史：
 */
public abstract class BaseFragment extends LazyFragment {

    private View rootView;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        if (rootView == null) {
            rootView = initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        setContentView(rootView);
        initEachData();

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
    /**
     * 初始化view
     *
     * @return 被加载的view
     */
    public abstract View initView();

    /**
     * 需要每次切换都对数据进行初始化的东西放在此方法中
     */
    public abstract void initEachData();

    public MyProgressDialog myProgressDialog;

    /**
     * 显示进度框
     *
     * @param ctx Context
     */
    public void showDialog(Context ctx) {
        if (myProgressDialog == null || !myProgressDialog.isShowing()) {
            myProgressDialog = MyProgressDialog.create(ctx).show();
        }
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
        SharedPreferences sp = getActivity().getSharedPreferences(MyKey.userConfig, Activity.MODE_PRIVATE);
        return !sp.getString("userKey", "").equals("");
    }

    /**
     * 登录
     */
    public void login(Intent intent) {
        intent.setClass(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
