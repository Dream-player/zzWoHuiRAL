package zz.wohui.wohui365.whcouldsupermarket.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import zz.wohui.wohui365.activity.OrderDetailsActivity;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.whcouldsupermarket.R;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    private IWXAPI api;
	private TextView tvPayResult;//展示微信支付结果
	private Button btnConfirm;//用户确认按钮，成功或失败跳转不同。

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
		assignViews();

    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
		initListener();
    }
	//加载布局
	private void assignViews(){
		tvPayResult = (TextView) findViewById(R.id.tv_wxP);
		btnConfirm = (Button) findViewById(R.id.btn_wxpe_confirm);
	}
	//初始化监听
	private void initListener(){
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpTOOrderDetail();//跳转到订单详情
			}
		});
	}

	/**
	 * 跳转到订单详情
	 */
	private void jumpTOOrderDetail() {
		Intent intent = new Intent(WXPayEntryActivity.this, OrderDetailsActivity.class);
		intent.putExtra("from","WXPayEntryActivity");
		startActivity(intent);
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtils.e("onPayFinish, errCode = " + resp.errCode+"\nerrStr:"+resp.errStr
		+"\ntransaction:"+resp.transaction+"\nopenId:"+resp.openId);
		String msg = "";

		if(resp.errCode == 0)
		{
			msg = "支付成功";
		}
		else if(resp.errCode == -1)
		{
			msg = "支付失败";
		}
		else if(resp.errCode == -2)
		{
			msg = "取消支付";
		}
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("微信支付结果:"+ msg);
//			builder.show();
//		}
		tvPayResult.setText(msg);
	}
}