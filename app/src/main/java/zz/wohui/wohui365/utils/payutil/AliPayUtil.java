package zz.wohui.zz.utils.payutil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alipay.sdk.app.PayTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import zz.wohui.zz.activity.OrderDetailsActivity;
import zz.wohui.zz.url.Urls;

public class AliPayUtil {

	// 商户PID
	public static final String PARTNER = "2088901145005764";
	// 商户收款账号
	public static final String SELLER = "1474010520@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN92/CYf7kaE6dSxSCxkSb3ug7ZpixkuOhqQhePO1tRN96rCKePOvpzEYJWBwPhcFoj+dvR7phZC1tn3C1JbKEtpJmafBGgdlXqFXg1N++fEURni7Y9MP0DZxRh3CZjadsWkuWMkvDm1jC9ZjvcdDi5AmosHjCDWtyErgFWbFPGhAgMBAAECgYEA3w1dUpC8vhcdb/glGmpgUP0q64EeHtxNkwZ/HozvUqsgc4mrVoCfMsuxvInvm/gPZl7ZTd++QKom2xoEawgihoWEeys2A6AgTDQyxfvxlRtA9/fF3ZU8+HBS8BzgvOvmnKCm/KNl1XHsHuk4DVB7P0X63ik14hYYW7IaJSVWDYECQQD3bcUVomzUlZwUTxMSgXxHn6v3LvgTRCKOd8IB/KaBPWLPGaOhsJn81fmv3mr2WyJdgoMmvMZ9ZKONxzPHZ0AlAkEA5zSzAeFIHuAzRFMH8zD4Abm2lRAgFgguvbeX0+CLvHxhtdU7Sq67VeZTYkgcqef88t1IMuHHsJj02ygWlu4EzQJAEXYUr4cr6QC56BEAhpDOxXs2NpaA+VMYoTdAYMWEtqAvZfAoRRPieh5bpZars8EQtsqsGMK/uz+r7yh8tfjKZQJAGdhcQlOKmlj5oiOd+eN5dcfqzxL0Y0Ia535EbJznQfStf4QMkrahnmKW45+oBJ44OnXeAkIW7njKtkzu6YRF6QJAVxybrqht2UFDGqogJhl6GGK8HZdZMFWnROpReMzDE7xgeiq9GJRT3oSIuDKXCV8Ywk532hjw8TrDXlGucIdw+Q==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	Activity ctx;

	public AliPayUtil(Activity activity) {
		ctx=activity;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(ctx, "支付成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ctx,OrderDetailsActivity.class);
					intent.putExtra("from","WXPayEntryActivity");
					ctx.startActivity(intent);
					ctx.finish();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ctx, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(ctx, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};



	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay(String orderNumber) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(ctx).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//

						}
					}).show();
			return;
		}
		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01",orderNumber);

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ctx);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 *
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(ctx);
		String version = payTask.getVersion();
		Toast.makeText(ctx, version, Toast.LENGTH_SHORT).show();
	}



	/**
	 * create the order info. 创建订单信息
	 *
	 */
	private String getOrderInfo(String subject, String body, String price ,String orderNumber) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNumber + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Urls.AliPayNotice+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}


	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		Log.e("待签名订单信息", content.replace("\"","\\\""));
		String sign = SignUtils.sign(content, RSA_PRIVATE);
		Log.e("yi签名订单信息", sign);
		return sign;
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
