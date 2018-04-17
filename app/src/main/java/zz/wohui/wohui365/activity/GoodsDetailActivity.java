package zz.wohui.wohui365.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * Created by zsy_18 on 2016/12/9.
 */

public class GoodsDetailActivity extends BaseActivity {
    private final String TAG = "GoodsDetailActivity";
    private String url;
    private String shopName;
    private String id;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_goods_detail);
        assignViews();
        initData();
        initView();
        initListener();
    }

    private LinearLayout llTitleMain;
    private RelativeLayout titleGoBack;
    private LinearLayout titleLLName;
    private TextView titleName;
    private EditText titleEdit;
    private RelativeLayout titleDoSearch;
    private ImageView ivTitleSearch;
    private ImageView ivTitleMore;
    private WebView web;

    private void assignViews() {
        llTitleMain = (LinearLayout) findViewById(R.id.llTitleMain);
        titleGoBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleLLName = (LinearLayout) findViewById(R.id.titleLLName);
        titleName = (TextView) findViewById(R.id.titleName);
        titleEdit = (EditText) findViewById(R.id.titleEdit);
        titleDoSearch = (RelativeLayout) findViewById(R.id.titleDoSearch);
        ivTitleSearch = (ImageView) findViewById(R.id.ivTitleSearch);
        ivTitleMore = (ImageView) findViewById(R.id.ivTitleMore);
        web = (WebView) findViewById(R.id.web_tuwenxiangqing);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        titleGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Last();
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        LogUtils.e(TAG, "shopName:" + shopName);
        if (shopName == null) {
            titleName.setText("图文详情");
        } else {
            titleName.setText(shopName);
        }
        if (url == null || "".equals(url)) {
            showToast(this, "该商品暂无详情!");
            return;
        }
        web.loadUrl("http://yc.wohui365.com/Mobile/GoodsDetail?Id="+id);
//        StringBuilder sb = new StringBuilder();
//        //拼接一段HTML代码
//        sb.append("<html>");
//        sb.append("<head>");
////        sb.append("<title>欢迎你</title>");
//        sb.append("</head>");
//        sb.append("<body>");
//        sb.append(url);
//        sb.append("</body>");
//        sb.append("</html>");
//        LogUtils.e(sb.toString());
////        web.loadData(sb.toString(), "text/html", "utf-8");
//        web.loadData(sb.toString(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
        web.requestFocusFromTouch();
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        settings.setDisplayZoomControls(false);//不显示webview缩放按钮
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        shopName = getIntent().getStringExtra("shopName");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

    }

    /**
     * 返回上一层
     */
    private void jump2Last() {
        this.onBackPressed();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            jump2Last();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        web.removeAllViews();
        web.destroy();
    }
}
