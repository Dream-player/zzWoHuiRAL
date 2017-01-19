package whcs.wohui.zz.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

/**
 * 说明：需要用到联网操作的类
 * 作者：陈杰宇
 * 时间： 2016/1/21 15:04
 * 版本：V1.0
 * 修改历史：
 */
public class MyOkHttpUtils {

    private Context ctx;
    private LruCache<String, Bitmap> lruCache;
    private File cacheDir;
    private ExecutorService threadPool;
    private Object tag;

    public MyOkHttpUtils(Context ctx) {
        this.ctx = ctx;
    }

    public MyOkHttpUtils() {

    }

    public MyOkHttpUtils(Object tag) {
        this.tag = tag;
    }

    /**
     * Post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 返回
     */
    public void postRequest(String url, MyRequestParams params, Callback callback) {
        Set keys = params.getQueryStringParams().keySet();
        if (keys != null) {
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = params.getQueryStringParams().get(key);
                LogUtils.outLog(key + "===" + value);
            }
        } else {
            LogUtils.outLog("params null");
        }

        OkHttpUtils
                .post()
                .url(url)
                .tag(tag)
                .params(params.getQueryStringParams())
                .build()
                .execute(callback);
    }

    /**
     * 加载图片的方法
     *
     * @param url       图片的路径
     * @param imageView 加载图片的控件
     */
    public void downLoadImage(final String url, final ImageView imageView) {

        if (lruCache == null) {
            int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);

            lruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
            threadPool = Executors.newFixedThreadPool(5);
            cacheDir = ctx.getFilesDir();
        }

        final String imageName = MD5Encoder.encode(url);
        Bitmap bitmap = lruCache.get(imageName);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File jpgFile = new File(cacheDir, imageName);
                    if (jpgFile.exists()) {
                        final Bitmap bitmapFile = BitmapFactory.decodeFile(jpgFile.getAbsolutePath());
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                int imageViewPosition = (Integer) imageView.getTag();
                                imageView.setImageBitmap(bitmapFile);
                            }
                        });

                        lruCache.put(imageName, bitmapFile);
                    } else {
                        doDownImageFile(url, imageView);
                    }
                }
            }).start();

            return;
        }
    }

    /**
     * 本地以及缓存中都没有图片进行下载并进行缓存
     *
     * @param url       下载路径
     * @param imageView 要显示控件
     */
    private void doDownImageFile(final String url, final ImageView imageView) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.outLog("downLoadImage OnError" + e.getMessage());
                        Toast.makeText(ctx, "下载图片失败，请检查您的网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        lruCache.put(MD5Encoder.encode(url), response);
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(cacheDir.getAbsolutePath() + "/" + MD5Encoder.encode(url));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        response.compress(Bitmap.CompressFormat.PNG, 70, outputStream);
                    }
                });
    }

    public void cancelHttp(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}
