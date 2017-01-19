package whcs.wohui.zz.utils.updateApp.multithread;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import whcs.wohui.zz.utils.updateApp.util.LogUtil;


public class DownloadThread extends Thread {

	private static final String TAG = "DownloadThread";

	private int threadId = -1;//初始化线程ID
	private int block;//每条线程下载的大小
	private int lengthDownloaded;//该线程已经下载的数据长度
	private boolean finished = false;//该线程是否已经下载完成

	private FileDownloader downloader;//文件下载器
	private URL urlDownloading;//下载的URL
	private File fileSaved;//保存下载的数据的文件

	public DownloadThread(FileDownloader downloader, URL urlDownloading,
			File fileSaved, int block, int lengthDownloaded, int threadId) {
		this.downloader = downloader;
		this.urlDownloading = urlDownloading;
		this.fileSaved = fileSaved;
		LogUtil.e(TAG, fileSaved.toString());
		this.block = block;
		this.lengthDownloaded = lengthDownloaded;
		this.threadId = threadId;
	}

	@Override
	public void run() {
		if(lengthDownloaded < block) {
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) urlDownloading.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("Charset", "UTF-8");//设置通信编码为UTF-8

				int startPosition = block * (threadId - 1) + lengthDownloaded;
				int endPosition = block * threadId - 1;
				//设置获取实体数据的范围，如果超过了实体数据的大小会自动返回实际的数据大小
				conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
				conn.setRequestProperty("Connection", "Keep-Alive");//使用长连接

				InputStream in = conn.getInputStream();
				byte[] buffer = new byte[1024];//设置本地数据缓存的大小为1MB
				int offset = 0;//设置每次读取的数据量
				LogUtil.e(TAG, "Thread " + threadId + " starts to download from position:" + startPosition);
				RandomAccessFile threadFile = new RandomAccessFile(fileSaved, "rwd");
				threadFile.seek(startPosition);//文件指针指向开始下载的位置
				while (!downloader.getExited() && (offset = in.read(buffer, 0, 1024)) != -1) {
					threadFile.write(buffer, 0, offset);
					lengthDownloaded += offset;
					downloader.update(threadId, lengthDownloaded);
					downloader.append(offset);
				}
				threadFile.close();
				in.close();
				if(downloader.getExited()) {
					LogUtil.e(TAG, "Thread " + threadId + " has been paused.");
				} else {
					LogUtil.e(TAG, "Thread " + threadId + " download finish.");
				}
				this.finished = true;
			} catch (Exception e) {
				lengthDownloaded = -1;//重新下载
				LogUtil.e(TAG, "Thread " + threadId + e);
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}
	}

	/**
	 * 该线程是否已下载完成
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * 已经下载的内容大小
	 * @return 如果返回值为-1，则代表下载失败
	 */
	public long getLengthDownloaded() {
		return lengthDownloaded;
	}

}
