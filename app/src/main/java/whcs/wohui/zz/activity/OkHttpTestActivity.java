package whcs.wohui.zz.activity;

import android.os.Bundle;
import android.widget.ImageView;

import de.greenrobot.event.EventBus;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/1/19 16:06
 * 版本：V1.0
 * 修改历史：
 */
public class OkHttpTestActivity extends BaseActivity{


    private ImageView imageView;
    private MyOkHttpUtils myOkHttpUtils;
    @Override
    public void myOnCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_okhttp_test);
        imageView = (ImageView) findViewById(R.id.image);
        EventBus.getDefault().register(this);
        myOkHttpUtils = new MyOkHttpUtils(this);
        downLoadImage();

    }

    /**
     * 下载图片
     */
    private void downLoadImage()
    {
        myOkHttpUtils.downLoadImage("http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg",
                                    imageView);
    }

    public void onEventMainThread(String event)
    {
        LogUtils.outLog(event + "okhttptest");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myOkHttpUtils != null){
            myOkHttpUtils = null;
            System.gc();
        }
        EventBus.getDefault().unregister(this);
    }
}
