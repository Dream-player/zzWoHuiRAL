package whcs.wohui.zz.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import whcs.wohui.zz.url.Urls;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.updateApp.multithread.DownloadProgressListener;
import whcs.wohui.zz.utils.updateApp.multithread.FileDownloader;
import whcs.wohui.zz.utils.updateApp.util.CommonUtil;
import whcs.wohui.zz.utils.updateApp.util.HttpUtil;
import whcs.wohui.zz.utils.updateApp.util.LogUtil;
import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/3/7 10:40
 * 版本：V1.0
 * 修改历史：
 */
public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";
    private String pathSaved;//apk文件保存根目录
    private ProgressDialog pd;//更新进度条
    private File apkFile;//下载文件


    /**
     * 跳转到主页
     */
    private void jump2Main() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(10000);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        PackageManager pm = getPackageManager();

        boolean flag = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "whcs.wohui.zz.whcouldsupermarket"));
        if (flag){
            LogUtils.e(TAG,"有权限");
        }else {
            LogUtils.e(TAG,"没有权限");
        }
        initProgressBar();
        //TODO 需要添加检测SD卡是否可用
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//进行权限判断
            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    66);
        }else {
            pathSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
            checkVersion(Urls.UpdateApp,CommonUtil.getVersionCodeClient(this),CommonUtil.getVersionName(this));
        }


    }
    /**
     * 初始化进度条
     */
    private void initProgressBar() {
        pd = new ProgressDialog(this);
        pd.setMessage("正在下载,请耐心等待");
        pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (fileDownloader!=null){
                    fileDownloader.exit();
                }
                WelcomeActivity.this.finish();
            }
        });
        pd.setIndeterminate(false);
        pd.setMax(100);
    }
    public void checkVersion(String checkUpdateUrl, final int versionCode, final String versionName) {
        if (checkUpdateUrl != null && versionCode != -1) {
            if (CommonUtil.isNetworkAvailable(this)) {
                HttpUtil.sendHttpRequest(checkUpdateUrl, new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onFinish(Message msg) {
                        if (msg.obj==null){
                            Toast.makeText(WelcomeActivity.this,"网络出错!",Toast.LENGTH_SHORT).show();
                            jump2Main();
                            return;
                        }
                        String response =  msg.obj.toString();
                        LogUtils.e(response+"***");
                        JSONObject json = null;
                        String desc = null;
                        String updateUrl = null;
                        boolean isImportent = false;
                        int updateVersion = -1;
                        try {
                            json = new JSONObject(response);
                            JSONObject data = json.getJSONObject("Data");
                            desc = data.getString("desc");
                            String strCode = data.getString("Code");
                            updateVersion = Integer.parseInt(strCode);
                            updateUrl = data.getString("update_url");
                            String strImportent = data.getString("isimport");
                            isImportent = Boolean.parseBoolean(strImportent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            jump2Main();
                            return;
                        }
                        if (versionCode < updateVersion&&isImportent) {

                            showUpdateDialog(updateUrl,versionName,desc);
                        }else {
                            //  2016/12/22 进行跳转
                            jump2Main();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                jump2Main();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 66) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pathSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
                checkVersion(Urls.UpdateApp,CommonUtil.getVersionCodeClient(this),CommonUtil.getVersionName(this));
            } else {
                jump2Main();
            }
        }
    }

    private void showUpdateDialog(final String updateUrl, String versionName, String desc) {
        String message = "检测到新版本：\n" + "当前版本：" + versionName
                + "，" + desc;
        AlertDialog.Builder adb = new AlertDialog.Builder(WelcomeActivity.this);

        adb.setTitle("版本更新");
        adb.setMessage(message);
        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(WelcomeActivity.this, "重要版本，必须更新", Toast.LENGTH_SHORT).show();
                WelcomeActivity.this.finish();
            }
        });
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectivityManager myConnectivityManager=
                        (ConnectivityManager) WelcomeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info=myConnectivityManager.getActiveNetworkInfo();
                if(info !=null && info.getType() ==  ConnectivityManager.TYPE_MOBILE){
                    isGoOnUpdataDialog(updateUrl,pathSaved);
                }else{
                    downloadApk(updateUrl,pathSaved);
                }
            }
        });
        adb.show();
    }
    /**
     * 当为手机网络联网的时候提示是否继续下载
     */
    protected void isGoOnUpdataDialog(final String updateUrl, final String pathSaved){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("警告!!!");
        adb.setMessage("当前网络为移动网络，是否继续下载");
        adb.setCancelable(false);
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               downloadApk(updateUrl,pathSaved);
            }
        });
        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(WelcomeActivity.this, "重要版本，必须更新", Toast.LENGTH_SHORT).show();
                WelcomeActivity.this.finish();
            }
        });
        adb.show();
    }
    private FileDownloader fileDownloader;
    private static final int DOWNLOADING = 1;
    private static final int DOWNLOAD_FALURE = -1;
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor
            (5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
    private void downloadApk(final String apkUrl, final String pathSavedDir) {
        pd.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWNLOADING:
                        int progress = msg.getData().getInt("size");
                        int fileSize = msg.getData().getInt("fileSize");
                        LogUtils.e(TAG,"progress:"+progress*100/fileSize);
                        pd.setProgress(progress*100/fileSize);
                        if (progress >= fileSize) {
                            LogUtil.e(TAG, "Download finished.");
                            //载完成
                            CommonUtil.installApk(WelcomeActivity.this, apkFile);
                        }
                        break;
                    case DOWNLOAD_FALURE:
                        break;
                }
            }
        };
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    fileDownloader = new FileDownloader
                            (WelcomeActivity.this, apkUrl, pathSavedDir, 3);
                    apkFile = fileDownloader.getApkFile();
                    final int fileSize = fileDownloader.getFileSize();
                    fileDownloader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {
                            Message msg = new Message();
                            msg.what = DOWNLOADING;
                            msg.getData().putInt("size", size);
                            msg.getData().putInt("fileSize",fileSize);
                            handler.sendMessage(msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendMessage(handler.obtainMessage(DOWNLOAD_FALURE));
                }
            }
        });
    }

}
