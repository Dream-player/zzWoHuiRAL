package whcs.wohui.zz.utils.updateApp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import android.os.Handler;
import android.os.Message;

public class HttpUtil {

	//构造一个线程池
	private static final ThreadPoolExecutor threadPoolExecutor =
			new ThreadPoolExecutor(2, 4, 3L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(2));

	public static void sendHttpRequest(final String address,
			final HttpCallBackListener listener) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				listener.onFinish(msg);
			}
		};

		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage(200);
				HttpURLConnection conn = null;
				try {
					URL url = new URL(address);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						InputStreamReader isr = new InputStreamReader(in, "UTF-8");
						BufferedReader reader = new BufferedReader(isr);
						StringBuffer result = new StringBuffer();
						String line;
						while((line = reader.readLine()) != null) {
							result.append(line);
						}
						if (listener != null) {
							msg.obj = result.toString();
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = 500;
					msg.obj = e;
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
				handler.sendMessage(msg);
			}
		});
	}

	public interface HttpCallBackListener {
		void onFinish(Message msg);
	}

}
