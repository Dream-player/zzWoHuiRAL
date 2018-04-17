package zz.wohui.zz.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zz.wohui.zz.adapter.SpinnerAdapter;
import zz.wohui.zz.myview.SpinnerPopWindow;
import zz.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：申请退款页面
 * 作者：陈杰宇
 * 时间： 2016/2/17 14:06
 * 版本：V1.0
 * 修改历史：
 */
public class ApplyRefundActivity extends BaseActivity implements View.OnClickListener, SpinnerAdapter.IOnItemSelectListener {

    private ScrollView srlApplySection;
    private RelativeLayout rlCheckReason;
    private TextView tvSelectReason;
    private LinearLayout btDropdown;
    private EditText etApplyPrice;
    private LinearLayout llApplyExplain;
    private EditText etApplyExplain;
    private Button btnSubmitApply;
    private LinearLayout llSuccessCue;
    private TextView tvCountDown;
    private TextView titleName;
    private RelativeLayout goBack;

    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        srlApplySection = (ScrollView) findViewById(R.id.srlApplySection);
        rlCheckReason = (RelativeLayout) findViewById(R.id.rlCheckReason);
        tvSelectReason = (TextView) findViewById(R.id.tvSelectReason);
        btDropdown = (LinearLayout) findViewById(R.id.bt_dropdown);
        etApplyPrice = (EditText) findViewById(R.id.etApplyPrice);
        llApplyExplain = (LinearLayout) findViewById(R.id.llApplyExplain);
        etApplyExplain = (EditText) findViewById(R.id.etApplyExplain);
        btnSubmitApply = (Button) findViewById(R.id.btnSubmitApply);
        llSuccessCue = (LinearLayout) findViewById(R.id.llSuccessCue);
        tvCountDown = (TextView) findViewById(R.id.tvCountDown);
    }


    private String[] reasons = {"太贵了", "质量问题", "不想要了", "其他"};
    private List<String> reasonList = new ArrayList<>();

    private Context ctx;
    private SpinnerAdapter spinnerAdapter;
    private SpinnerPopWindow mSpinnerPopWindow;
    private int countDownTime = 0;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_apply_refund);
        assignViews();
        ctx = this;

        initReasonSpinner();
        goBack.setOnClickListener(this);
        btnSubmitApply.setOnClickListener(this);
        tvSelectReason.setOnClickListener(this);

    }

    /**
     * 初始化申请原因选择器
     */
    private void initReasonSpinner() {
        for (String resaon : reasons) {
            reasonList.add(resaon);
        }
        spinnerAdapter = new SpinnerAdapter(ctx, reasonList);
        spinnerAdapter.refreshData(reasonList, 0);
        mSpinnerPopWindow = new SpinnerPopWindow(this);
        mSpinnerPopWindow.setAdatper(spinnerAdapter);
        mSpinnerPopWindow.setItemListener(this);
    }

    private void showSpinnerWindow() {
        mSpinnerPopWindow.setWidth(tvSelectReason.getWidth());
        mSpinnerPopWindow.showAsDropDown(tvSelectReason);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelectReason:

                showSpinnerWindow();

                break;
            case R.id.titleGoBack:
                ApplyRefundActivity.this.onBackPressed();
                finish();
                break;
            case R.id.btnSubmitApply:

                String strApplyReason = tvSelectReason.getText().toString().trim();
                if (strApplyReason.equals("请选择退款原因") || strApplyReason.equals("")) {
                    Toast.makeText(this, "请选择退款原因", Toast.LENGTH_SHORT).show();
                    break;
                }
                String strRefundPrice = etApplyPrice.getText().toString().trim();
                if (strRefundPrice.equals("") || strRefundPrice.equals("0")
                        || Double.parseDouble(strRefundPrice) < 0) {
                    Toast.makeText(ctx, "请输入正确的退款金额", Toast.LENGTH_SHORT).show();
                    break;
                }
                String strRefundExplain = etApplyExplain.getText().toString().trim();
                if (strRefundExplain.equals("")) {
                    Toast.makeText(ctx, "请填写退款说明", Toast.LENGTH_SHORT).show();
                    break;
                }
                submitApply(strApplyReason, strRefundPrice, strRefundExplain);
                break;
        }
    }

    /**
     * 提交退款申请
     *
     * @param strApplyReason   申请原因
     * @param strRefundPrice   申请金额
     * @param strRefundExplain 申请说明
     */
    private void submitApply(String strApplyReason, String strRefundPrice, String strRefundExplain) {
        // TODO: 2016/2/17 提交退款申请

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ApplyRefundActivity.this
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        // TODO: 2016/2/17 申请成功出现成功页面
        showSuccessCue();
    }

    /**
     * 显示申请成功页面 并开始倒计时
     */
    private void showSuccessCue() {
        llSuccessCue.setVisibility(View.VISIBLE);
        srlApplySection.setVisibility(View.GONE);
        countDownTime = 5;
        tvCountDown.setText(countDownTime + "");
        handler.sendEmptyMessageDelayed(1000, 1000);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    if (countDownTime > 0) {
                        tvCountDown.setText((countDownTime - 1) + "");
                        countDownTime--;
                        handler.sendEmptyMessageDelayed(1000, 1000);
                    } else {
                        ApplyRefundActivity.this.onBackPressed();
                        finish();
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };


    /**
     * 自定义的spinner的点击事件
     *
     * @param pos
     */
    @Override
    public void onItemClick(int pos) {
        if (pos >= 0 && pos <= reasonList.size()) {
            String value = reasonList.get(pos);
            tvSelectReason.setText(value.toString());
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(1000);
        super.onDestroy();
    }
}
