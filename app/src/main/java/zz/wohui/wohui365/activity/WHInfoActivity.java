package zz.wohui.wohui365.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zz.wohui.wohui365.R;


/**
 * 关于我惠页面
 *
 * @author MAINTEL
 *
 *         DATE:2015-12-10
 *
 *         下午2:52:23
 *
 */
public class WHInfoActivity extends BaseActivity {

	private RelativeLayout goBack;
	private TextView titleName;
	@Override
	public void myOnCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_wohui_info);
		goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
		titleName = (TextView) findViewById(R.id.titleName);
		titleName.setText("关于我惠");
		titleName.setGravity(Gravity.CENTER);
		goBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				WHInfoActivity.this.onBackPressed();
				finish();

			}
		});
	}


}
