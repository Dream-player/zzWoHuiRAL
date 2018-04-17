package zz.wohui.wohui365.utils.updateApp.multithread;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import android.content.Context;

import zz.wohui.wohui365.utils.updateApp.db.FileService;
import zz.wohui.wohui365.utils.updateApp.util.LogUtil;

/**
 * 多线程、断点下载
 * @author KID
 *
 */
public class FileDownloader {
	private static final String TAG = "FileDownloader";

	private Context context;
	private FileService fileService;//获取本地数据库的业务Bean
	private String downloadUrl;//下载路径
	private int fileSize = 0;//下载的文件的长度
	private File fileSaved;//保存下载的数据的本地文件

	private DownloadThread[] threads;//根据线程数设置下载线程池
	private int block;//每条线程下载的长度
	private Map<Integer, Integer> data = new ConcurrentHashMap
			<Integer, Integer>();//缓存各线程已下载的文件长度
	private int fileSizeDownloaded = 0;//已下载的文件长度（总）
	private boolean exited = false;//停止下载标志

	private File apkFile;

	/**
	 * 使用同步关键字解决并发线程访问问题
	 * @param size
	 */
	protected synchronized void append(int size) {
		fileSizeDownloaded += size;//把实时下载的长度加到总下载长度中
	}

	/**
	 * 更新指定线程最后的下载位置
	 * @param threadId
	 * @param position
	 */
	protected synchronized void update(int threadId, int position) {
		data.put(threadId, position);
		fileService.update(downloadUrl, threadId, position);
	}

	public int getFileSize() {
		return fileSize;
	}

	public int getThreadSize() {
		return threads.length;
	}

	public void exit() {
		this.exited = true;
	}
	public void start(){this.exited = false; }
	public boolean getExited() {
		return exited;
	}

	public File getApkFile() {
		return apkFile;
	}

	public void setApkFile(File apkFile) {
		this.apkFile = apkFile;
	}


	/**
	 * 文件下载器
	 * @param context
	 * @param downloadUrl 下载路径
	 * @param fileSavedDir 文件保存目录
	 * @param threadCount 下载线程数
	 */
	public FileDownloader(Context context, String downloadUrl,
			String fileSavedDir, int threadCount) {
		try {
			this.context = context;
			this.downloadUrl = downloadUrl;
			fileService = new FileService(this.context);
			URL url = new URL(downloadUrl);

			threads = new DownloadThread[threadCount];

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.connect();
			printResponseHeader(conn);
			if (conn.getResponseCode() == 200) {
				fileSize = conn.getContentLength();
				if (fileSize <= 0) throw new RuntimeException("Unknown file size.");

				File file = new File(fileSavedDir);
				if(!file.exists()) file.mkdirs();
				String fileName = getFileName(downloadUrl, conn);
				fileSaved = new File(fileSavedDir + "/" + fileName);
				setApkFile(fileSaved);
				LogUtil.e(TAG, fileSaved.toString());

				Map<Integer, Integer> logData = fileService.getData(downloadUrl);
				if (logData.size() > 0) {
					for (Map.Entry<Integer, Integer> entry : logData.entrySet()) {
						data.put(entry.getKey(), entry.getValue());
					}
				}

				if(data.size() == threads.length) {
					for (int i = 0; i < threads.length; i++) {
						fileSizeDownloaded += data.get(i + 1);
					}
				}
				block = (fileSize % threads.length) == 0 ? fileSize / threads.length
						: fileSize / threads.length + 1;
			} else {
				throw new RuntimeException("server response error.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Can't connect to this url.");
		}
	}

	public int download(DownloadProgressListener listener) throws Exception {
		try {
			RandomAccessFile raf = new RandomAccessFile(fileSaved, "rwd");
			if(fileSize > 0) {
				raf.setLength(fileSize);
			}
			raf.close();//关闭该文件，使设置生效
			URL url = new URL(downloadUrl);

			if(data.size() != threads.length) {
				data.clear();
				for(int i = 0;i < threads.length;i++) {
					data.put(i + 1, 0);
				}
				fileSizeDownloaded = 0;
			}

			/**
			 * 开启线程进行下载
			 */
			for(int i = 0; i < threads.length; i++) {
				int lengthDownloaded = data.get(i + 1);
				if (lengthDownloaded < block && fileSizeDownloaded < fileSize) {
					threads[i] = new DownloadThread(this, url,
							fileSaved, block, data.get(i + 1), i + 1);
					threads[i].setPriority(7);
					threads[i].start();
				} else {
					threads[i] = null;//表明线程已完成下载
				}
			}
			fileService.delete(downloadUrl);
			fileService.save(downloadUrl, data);
			boolean notFinished = true;
			while (notFinished) {
				Thread.sleep(900);
				notFinished = false;
				for (int i = 0; i < threads.length; i++) {
					if(threads[i] != null && !threads[i].isFinished()) {
						notFinished = true;
						if (threads[i].getLengthDownloaded() == -1) {
							threads[i] = new DownloadThread(this, url, fileSaved,
									block, data.get(i + 1), i+ 1);
							threads[i].setPriority(7);
							threads[i].start();
						}
					}
				}
				if (listener != null) listener.onDownloadSize(fileSizeDownloaded);
			}
			if (fileSizeDownloaded == fileSize) fileService.delete(downloadUrl);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("File downloads error");
		}
		return fileSizeDownloaded;
	}

	public String getFileName(String downloadUrl, HttpURLConnection conn) {
		String fileName = null;
		if(downloadUrl != null) {
			int start = downloadUrl.lastIndexOf("/");
			if(start != -1) {
				fileName = downloadUrl.substring(start + 1);
				if(fileName == null || "".equals(fileName.trim())) {
					for(int i= 0;; i++) {
						String mine = conn.getHeaderField(i);
						if(mine == null) break;
						if("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
							Matcher matcher = Pattern.compile(".*fileName=(.*)")
									.matcher(mine.toLowerCase());
							if(matcher.find()) return matcher.group(1);
						}
					}

				}
			} else {
				fileName = UUID.randomUUID() + ".tem";
			}
		}
		return fileName;
	}

	public static Map<String, String> getHttpResponseHeader(HttpURLConnection conn) {
		//使用LinkedHashMap保证写入和遍历的时候的顺序相同，而且允许空值存在
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String fieldValue = conn.getHeaderField(i);
			if (fieldValue == null) break;
			header.put(conn.getHeaderFieldKey(i), fieldValue);
		}
		return header;
	}

	public static void printResponseHeader(HttpURLConnection conn) {
		Map<String, String> header = getHttpResponseHeader(conn);
		for(Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getValue() + ":" : "";
			LogUtil.e(TAG, key + entry.getValue());
		}
	}
}