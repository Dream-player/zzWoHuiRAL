package whcs.wohui.zz.utils.updateApp.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.updateApp.multithread.DownloadProgressListener;
import whcs.wohui.zz.utils.updateApp.multithread.FileDownloader;
import whcs.wohui.zz.utils.updateApp.util.CommonUtil;
import whcs.wohui.zz.utils.updateApp.util.LogUtil;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/6/27 16:07
 * 版本：V1.0
 * 修改历史：
 */
public class DownloadService extends Service{
    public NotificationManager mNotificationManager;//通知管理
    NotificationCompat.Builder mBuilder;
    private static final int DOWNLOADING = 1;
    private static final int DOWNLOAD_FALURE = -1;
    private File apkFile;
    private int progress = 0;
    /** Notification的ID */
    int notifyId = 102;
    private FileDownloader fileDownloader;
    private static final String TAG = "DownloadService";

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor
            (5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
    private String apkUrl;
    private String pathSavedDir;
    private MyBinder myBinder = new MyBinder();
    public static final String ACTION_START_DOWNLOADER = "whcs.wohui.zz.utils.updateApp.todo.DownloadService.start";
    @Override
    public void onCreate() {
        super.onCreate();

        initNotifyService();
        initNotify();
        initDownloaderReceiver();
        Toast.makeText(this,"服务create",Toast.LENGTH_SHORT).show();
        LogUtils.e("服务create********************");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"服务start",Toast.LENGTH_SHORT).show();
        apkUrl = intent.getStringExtra("apkUrl");
        pathSavedDir = intent.getStringExtra("pathSavedDir");
//        downloadApk(apkUrl,pathSavedDir);
        LogUtils.e("**************************************************************");
        LogUtils.e("apkUrl:"+apkUrl+"pathSavedDir"+pathSavedDir);
        return super.onStartCommand(intent, flags, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        apkUrl = intent.getStringExtra("apkUrl");
        pathSavedDir = intent.getStringExtra("pathSavedDir");
        LogUtils.e("服务bind****************************");
        return myBinder;
    }
    /**
     * 初始化要用到的系统服务
     */
    private void initNotifyService() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(this);
        Intent stopDownloaderIntent = new Intent(ACTION_START_DOWNLOADER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, stopDownloaderIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(pendingIntent)
                        // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                        // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                        // requires VIBRATE permission

//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentInfo("版本更新")
                .setContentTitle("等待下载")
                .setContentText("进度:" + "0/0")
                .setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的


        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性:
     * 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    private void downloadApk(final String apkUrl, final String pathSavedDir) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWNLOADING:
                        int progress = msg.getData().getInt("size");
                        int fileSize = msg.getData().getInt("fileSize");
                        setNotify(progress,fileSize);
                        if (progress >= fileSize) {
                            LogUtil.e(TAG, "Download finished.");
                            //TODO 下载完成
                            mBuilder.setContentTitle("下载完成");
                            mNotificationManager.notify(notifyId, mBuilder.build());
                            CommonUtil.installApk(DownloadService.this, apkFile);
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
                            (DownloadService.this, apkUrl, pathSavedDir, 3);
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
    /** 设置下载进度 */
    public void setNotify(int progress ,int fileSize) {
        float progressF = (float)(progress/10000)/100;//把单位转化为M，保留两位小数
        float fileSizeF = (float)(fileSize/10000)/100;
        mBuilder.setProgress(fileSize, progress, false) // 这个方法是显示进度条
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                .setContentTitle("下载中")
                .setContentText("进度:" + progressF + "M/" + fileSizeF + "M");
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(notifyId, mNotification);
    }
    public void setDownloadPauseNotify(){
        mBuilder.setContentTitle("暂停下载");
        mNotificationManager.notify(notifyId,mBuilder.build());
    }
    class MyBinder extends Binder {
        public void startDownload(){
            downloadApk(apkUrl,pathSavedDir);
        }
    }
    public void initDownloaderReceiver(){
        DownloaderReceiver myReceiver = new DownloaderReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_START_DOWNLOADER);
        registerReceiver(myReceiver,intentFilter);
    }
    class DownloaderReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("停止下载");
            if(!fileDownloader.getExited()){
                fileDownloader.exit();
                setDownloadPauseNotify();
                LogUtils.e("停止下载");

            }else{
                LogUtils.e("开始下载");
                fileDownloader.start();
                downloadApk(apkUrl,pathSavedDir);
            }
        }
    }

}
