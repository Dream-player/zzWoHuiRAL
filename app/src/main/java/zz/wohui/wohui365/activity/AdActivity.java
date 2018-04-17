package zz.wohui.wohui365.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zz.wohui.wohui365.R;


/**
 * Created by zsy_18 on 2017/4/23.
 */
public class AdActivity extends BaseActivity{

    private WebView mWebView;
    private TextView titleName;
    private Context ctx;
    private RelativeLayout titleGoBack;
    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ad);
        ctx=this;
        Intent intent =getIntent();
        String position =intent.getStringExtra("position");

        mWebView = (WebView) findViewById(R.id.webView1);
        titleName = (TextView) findViewById(R.id.titleName);
        titleGoBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBar);
        titleGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Last();
            }
        });

        titleName.setText("我惠超市");


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (position.equals("1")) {
            String url = "http://www.wohui365.com/";
            String url1 = "https://www.baidu.com/";

            mWebView.loadUrl(url);
        }else if (position.equals("2")){
            String url = "http://www.wohui365.com/";
            String url1 = "https://www.baidu.com/";

            mWebView.loadUrl(url);
        }else {
            String url1 = "https://www.baidu.com/";

            mWebView.loadUrl(url1);
        }
        mWebView.setWebViewClient(new WebViewClient(){
            //当点击链接时 希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                showDialog(ctx);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                dismissDialog();
		       }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
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
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}
