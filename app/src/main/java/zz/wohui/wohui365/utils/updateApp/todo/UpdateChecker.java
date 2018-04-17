package zz.wohui.wohui365.utils.updateApp.todo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.utils.updateApp.util.CommonUtil;
import zz.wohui.wohui365.utils.updateApp.util.HttpUtil;
import zz.wohui.wohui365.utils.updateApp.util.LogUtil;

public class UpdateChecker {

	private static final String TAG = "UpdateChecker";
	public static final int REQ_WIRELESS_SETTINGS = 1;//网络设置

	private Context context;
	private int versionCodeClient;//客户端版本号

	private String pathSaved;//apk文件保存根目录





	public UpdateChecker(Context context, int versionCodeClient) {
		this.context = context;
		this.versionCodeClient = versionCodeClient;
//		initService();//初始化进度条
		//TODO 需要添加检测SD卡是否可用
		pathSaved = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
	}

	public void checkVersion(String url_versionxml, final String versionName) {
		if (url_versionxml != null && versionCodeClient != -1) {
			if (CommonUtil.isNetworkAvailable(context)) {
				HttpUtil.sendHttpRequest(url_versionxml, new HttpUtil.HttpCallBackListener() {
					@Override
					public void onFinish(Message msg) {
						if (msg.obj==null){
							Toast.makeText(context,"网络出错!",Toast.LENGTH_SHORT).show();
							return;
						}
						String response =  msg.obj.toString();
						LogUtils.e(response+"***");
						JSONObject json = null;
						String desc = null;
						String updateUrl = null;
						int updateVersion = -1;
						try {
							json = new JSONObject(response);
							JSONObject data = json.getJSONObject("Data");
							desc = data.getString("desc");
							String strCode = data.getString("Code");
							updateVersion = Integer.parseInt(strCode);
							updateUrl = data.getString("update_url");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						LogUtils.e("versionCodeClient:"+versionCodeClient
						+"updateVersion:"+updateVersion);
						if (versionCodeClient < updateVersion) {
							showUpdateDialog(updateUrl,versionName,desc);
						}else {
							Toast.makeText(context,"暂无更新",Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else {
				Toast.makeText(context, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				((Activity) context).startActivityForResult(new Intent(Settings
						.ACTION_WIRELESS_SETTINGS), REQ_WIRELESS_SETTINGS);
			}
		}
	}

	private void showUpdateDialog(final String updateUrl,String versionName,String desc) {
		String message = "检测到新版本：\n" + "当前版本：" + versionName
				+ "，" + desc;
		MyUtils.showAlertDialog(context, "版本更新", message, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtil.e(TAG, updateUrl);
				Toast.makeText(context,"下载任务已添加到通知栏",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context,DownloadService.class);
				intent.putExtra("apkUrl",updateUrl);
				intent.putExtra("pathSavedDir", pathSaved);
				context.startService(intent);
				context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
			}
		});
//		View view = ((Activity) context).getLayoutInflater().inflate(
//				R.layout.dialog_update, null, false);
//		final TextView version = (TextView) view.findViewById(R.id.tv_version);
//		final TextView wirelessSetting = (TextView) view.findViewById(R.id.tv_wireless_setting);
//		final TextView downloadApk = (TextView) view.findViewById(R.id.tv_download_apk);
//		version.setText("检测到新版本：\n" + "当前版本：" + versionCodeClient
//				+ "，最新版本" + desc);
//		view.setFocusableInTouchMode(true);
//		view.requestFocus();
//		final Dialog dialog = new Dialog(context, R.style.myDialogTheme);
//		OnClickListener listener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//				case R.id.tv_wireless_setting:
////					((Activity) context).startActivityForResult(new Intent(Settings
////							.ACTION_WIRELESS_SETTINGS), REQ_WIRELESS_SETTINGS);
//					dialog.dismiss();
//					break;
//				case R.id.tv_download_apk:
//					LogUtil.e(TAG, updateUrl);
//					Toast.makeText(context,"下载任务已添加到通知栏",Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(context,DownloadService.class);
//					intent.putExtra("apkUrl",updateUrl);
//					intent.putExtra("pathSavedDir", pathSaved);
//					context.startService(intent);
//					context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
//					dialog.dismiss();
//				}
//			}
//		};
//		wirelessSetting.setOnClickListener(listener);
//		downloadApk.setOnClickListener(listener);
//
//		dialog.setContentView(view);
//		dialog.setCancelable(true);
//		dialog.show();
	}
	DownloadService.MyBinder myBinder;//用于与DowndoadService服务类进行通信
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (DownloadService.MyBinder) service;
			myBinder.startDownload();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

}
