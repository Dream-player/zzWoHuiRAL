package zz.wohui.wohui365.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import zz.wohui.wohui365.R;

/**
 * 说明：修改密码页面
 * 作者：陈杰宇
 * 时间： 2016/2/18 16:10
 * 版本：V1.0
 * 修改历史：
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    private EditText etOldPwd;
    private EditText etNewPwd;
    private EditText etConfirmNewPwd;
    private Button btnSubmitChange;
    private TextView titleName;
    private RelativeLayout goBack;

    private void assignViews() {
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        titleName = (TextView) findViewById(R.id.titleName);
        etOldPwd = (EditText) findViewById(R.id.etOldPwd);
        etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        etConfirmNewPwd = (EditText) findViewById(R.id.etConfirmNewPwd);
        btnSubmitChange = (Button) findViewById(R.id.btnSubmitChange);
    }

    private boolean isChangePayPwd = false;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_pwd);
        assignViews();
        titleName.setText("修改密码");
        Intent intent = getIntent();
        isChangePayPwd = intent.getBooleanExtra("isChangePayPwd", false);
        goBack.setOnClickListener(this);
        btnSubmitChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                this.onBackPressed();
                finish();
                break;
            case R.id.btnSubmitChange:

                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                String oldPwd = etOldPwd.getText().toString().trim();
                if (oldPwd.equals("")) {
                    Toast.makeText(this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String newPwd = etNewPwd.getText().toString().trim();
                if (newPwd.equals("")) {
                    Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String newPwd2 = etConfirmNewPwd.getText().toString().trim();
                if (newPwd.equals("")) {
                    Toast.makeText(this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!newPwd.equals(newPwd2)) {
                    Toast.makeText(this, "两次输入的新密码不相同", Toast.LENGTH_SHORT).show();
                    break;
                }
                // TODO: 2016/2/18  根据isChangePayPwd值来确认修改登录密码或者支付密码
                submitChange();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void submitChange() {

    }
}
