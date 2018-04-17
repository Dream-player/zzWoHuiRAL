package zz.wohui.wohui365.utils.updateApp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.File;

public class CommonUtil {

	/**
	 * 检查网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		Boolean connected = false;
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager != null) {
			NetworkInfo info = connManager.getActiveNetworkInfo();
			if (info != null) {
				connected = info.isConnected();
			}
		}
		return connected;
	}

	/**
	 * 安装apk
	 * @param context
	 * @param apkFile
	 */
	public static void installApk(Context context, File apkFile) {
		if (!apkFile.exists()) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 删除文件
	 * @param f
	 */
	public static  void deleteAllFiles(File f) {
        if(f.exists()) {
            File[] files = f.listFiles();
            if(files != null) {
                for(File file : files)
                    if(file.isDirectory()) {
                        deleteAllFiles(file);
                        file.delete();
                    }
                    else if(file.isFile()) {
                        file.delete();
                    }
            }
            f.delete();
        }
    }

	/**
	 * 获得客户端版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCodeClient(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context
					.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 获得版本名称
	 */
	public static String getVersionName(Context context){
		try {
			return context.getPackageManager().getPackageInfo(context
					.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1";
		}
	}
}
