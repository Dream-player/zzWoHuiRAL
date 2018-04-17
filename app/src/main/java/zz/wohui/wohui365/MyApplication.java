package zz.wohui.wohui365.whcouldsupermarket;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhy.http.okhttp.OkHttpUtils;


import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import zz.wohui.wohui365.service.LocationService;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.ShopCartDatabaseHelper;


/**
 * 作者：陈杰宇
 * 时间： 2016/1/5 12:47
 */
public class MyApplication extends Application {


    public LocationService locationService;
    public Vibrator mVibrator;

    private String address;
    private String city;
    private LatLng latLng;
    private SharedPreferences sp;
    private ShopCartDatabaseHelper myDBHelper;
    public ShopCartDatabaseHelper getMyDBHelper(String shopSerialNo){
        if (myDBHelper==null){
            myDBHelper = new ShopCartDatabaseHelper(userKey,shopSerialNo,this);
        }else{
            myDBHelper.setShopSerialNo(shopSerialNo);
        }
        return myDBHelper;
    }

    public String getUserKey() {

        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    private String userKey;

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化定位sdk，
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        Context context = getApplicationContext();
        SDKInitializer.initialize(context);
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        client.newBuilder().connectTimeout(8000, TimeUnit.MILLISECONDS);
        sp = getSharedPreferences(MyKey.userConfig, MODE_PRIVATE);
        userKey = sp.getString("userKey", "");
        initImageLoader();//初始化ImageLoader
    }

    /**
     * 初始化ImageLoader基本配置
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache"); //缓存文件的存放地址
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // max width, max height
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)  //降低线程的优先级保证主UI线程不受太大影响
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(5 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);//将配置设置给ImageLoader的单例
    }

}
