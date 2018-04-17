package zz.wohui.wohui365.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 说明：工具类  存放一些通用的方法
 * 作者：陈杰宇
 * 时间： 2016/1/31 16:00
 * 版本：V1.0
 * 修改历史：
 */
public class MyUtils {
    private static final String TAG = "MyUtils";
    /**
     * 初始化下来刷新列表
     *
     * @param pullList 被初始化的列表
     * @param ctx      上下文
     */
    public static void initPullList(PullToRefreshListView pullList, Context ctx) {
        pullList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        pullList.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        ILoadingLayout startLabels = pullList.getLoadingLayoutProxy();
        startLabels.setPullLabel("继续上划加载更多");
        startLabels.setRefreshingLabel("松开加载更多");
        startLabels.setRefreshingLabel("正在加载更多...");
    }
    public static AlertDialog.Builder adb;
    /**
     * 弹出提示框
     *
     * @param ctx             上下文
     * @param title           提示标题
     * @param msg             提示信息
     * @param onClickListener 确定点击事件
     */
    public static void showAlertDialog(Context ctx,
                                       String title,
                                       CharSequence msg,
                                       DialogInterface.OnClickListener onClickListener) {
        adb = new AlertDialog.Builder(ctx);
        adb.setTitle(title);
        adb.setMessage(msg);
        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setPositiveButton("确定", onClickListener);
        adb.show();
    }

    /**
     * 判断是否需要隐藏软键盘
     *
     * @param v  view
     * @param ev 点击事件
     * @return
     */
    public static boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param token
     */
    public static void HideSoftInput(IBinder token, Context ctx) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String networkError = "连接网络失败，请检查网络后重试";
    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        double d =b1.add(b2).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        LogUtils.e(TAG,"加结果:"+d);
        return d;

    }
    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        double d =b1.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        LogUtils.e(TAG,"减结果:"+d);
        return d;
    }
    /**
     * 两个时间字符串只差,返回单位分钟
     */
    public static int minBetween(String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        long dif = 0;
        try {
            Date start = sdf.parse(startTime);
            Date end = new Date();
            dif = (end.getTime()-start.getTime())/(1000*60);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return Integer.parseInt(String.valueOf(dif));
    }

    /**
     * 转换时间格式
     * @param strTime
     * @return
     */
    public static String switchTimeFormat(String strTime){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            date = sdf1.parse(strTime);
            return sdf2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return strTime;
        }



    }
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        double d = b1.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        LogUtils.e(TAG,"乘结果:"+d);
        return d;
    }
}
