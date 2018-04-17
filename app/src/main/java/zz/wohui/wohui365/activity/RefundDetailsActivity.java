package zz.wohui.wohui365.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zz.wohui.wohui365.R;

/**
 * 说明：退款详情页面
 * 作者：陈杰宇
 * 时间： 2016/2/17 9:46
 * 版本：V1.0
 * 修改历史：
 */
public class RefundDetailsActivity extends BaseActivity {

    private TextView tvRefundStatus;
    private TextView tvRefundPrice;
    private TextView tvShopName;
    private TextView tvGoodsName;
    private TextView tvOrderPrice;
    private TextView tvOrderTime;
    private TextView tvOrderNum;
    private TextView tvOrderCreateTime;
    private TextView tvApplyTime;
    private TextView titleName;
    private RelativeLayout goBack;

    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        tvRefundStatus = (TextView) findViewById(R.id.tvRefundStatus);
        tvRefundPrice = (TextView) findViewById(R.id.tvRefundPrice);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvGoodsName = (TextView) findViewById(R.id.tvGoodsName);
        tvOrderPrice = (TextView) findViewById(R.id.tvOrderPrice);
        tvOrderTime = (TextView) findViewById(R.id.tvOrderTime);
        tvOrderNum = (TextView) findViewById(R.id.tvOrderNum);
        tvOrderCreateTime = (TextView) findViewById(R.id.tvOrderCreateTime);
        tvApplyTime = (TextView) findViewById(R.id.tvApplyTime);
    }

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refund_details);
        assignViews();
        titleName.setText("退款详情");

        // TODO: 2016/2/17 根据传递的信息或者联网获取详情信息初始化内容
        initView();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefundDetailsActivity.this.onBackPressed();
                finish();
            }
        });
    }

    /**
     * 初始化内容
     */
    private void initView() {

    }
}
