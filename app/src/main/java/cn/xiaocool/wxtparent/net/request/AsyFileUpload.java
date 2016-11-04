/**
 * 
 */
package cn.xiaocool.wxtparent.net.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


/** 
 * @author long_xia@loongjoy.com
 * @version 创建时间：2015年11月27日 下午1:57:56 
 * 
 */
/**
 * @author long_xia@loongjoy.com
 *
 */
public class AsyFileUpload extends AsyncTask<String, Integer, String> {
	FileUploadCallBackLisenter lisenter;
	private List<String> fileList;

	/**
	 * @param execute两个参数
	 *            ：string filepath,string url;
	 * @param fileUploadCallBackLisenter
	 */

	public AsyFileUpload(FileUploadCallBackLisenter fileUploadCallBackLisenter) {
		this.lisenter = fileUploadCallBackLisenter;
	}

	/**
	 * getter method
	 * 
	 * @return the fileList
	 */

	public List<String> getFileList() {
		return fileList;
	}

	/**
	 * setter method
	 * 
	 * @param fileList
	 *            the fileList to set
	 */

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public interface FileUploadCallBackLisenter {
		public void callBack(String value);

		public void ProgressUpdate(int values);
	}

	@Override
	protected void onPostExecute(String result) {
		lisenter.callBack(result);

	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		values[0]--;
		if (values[0] < 0) {
			values[0] = 0;
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String uploadUrl = params[0];
		String end = "\r\n";
		String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(100 * 1000);
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			dos.writeBytes(end);
			for (int i = 0; i < fileList.size(); i++) {
				if (!fileList.get(i).equals("")) {
					dos.writeBytes("--" + boundary + "\r\n");
					String strs = "Content-Disposition: form-data; name=\"file[" + i + "]\"; filename=\""
							+ fileList.get(i).substring(fileList.get(i).lastIndexOf("/") + 1) + "\"" + end;
					dos.writeBytes(strs);
					dos.writeBytes("Content-Type: application/octet-stream" + end);
					dos.writeBytes("\r\n");
					File tempFile = new File("/sdcard/loongjoyApp/Cache/temp");
					FileOutputStream baos = new FileOutputStream(tempFile);
					Bitmap bitmapTemp = getSmallBitmap(fileList.get(i));
					// Bitmap bitmapTemp =
					// BitmapFactory.decodeFile(fileList.get(i));
					int heightRatio = Math.round((float) bitmapTemp.getHeight() / (float) 1280);
					int widthRatio = Math.round((float) bitmapTemp.getWidth() / (float) 720);
					if (widthRatio > 1 || heightRatio > 1) {
						Bitmap bitmap = Bitmap.createScaledBitmap(bitmapTemp,
								widthRatio > heightRatio ? bitmapTemp.getWidth() / heightRatio : bitmapTemp.getWidth()
										/ widthRatio, widthRatio > heightRatio ? bitmapTemp.getHeight() / heightRatio
										: bitmapTemp.getHeight() / widthRatio, true);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					} else {
						bitmapTemp.compress(Bitmap.CompressFormat.PNG, 50, baos);
					}

					baos.close();
					InputStream bis = new FileInputStream(tempFile);
					long total = bis.available();
					String totalstr = String.valueOf(total / 1024);
					byte[] buffer = new byte[8192]; // 8k
					int count = 0;
					int length = 0;
					while ((count = bis.read(buffer)) != -1) {
						dos.write(buffer, 0, count);
						length += count;
						publishProgress((int) ((length / (float) total) * 100));
						// 为了演示进度,休眠500毫秒
						// Thread.sleep(300);
					}
					bis.close();
					dos.writeBytes("\r\n");
					lisenter.ProgressUpdate(0);
				}

			}

			dos.writeBytes("--" + boundary + "--" + "\r\n");
			dos.writeBytes("\r\n");
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			if (httpURLConnection.getContentEncoding() != null && httpURLConnection.getContentEncoding() != null
					&& httpURLConnection.getContentEncoding().contains("gzip")) {
				is = new GZIPInputStream(is);
			}
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			@SuppressWarnings("unused")
			String result = br.readLine();
			dos.close();
			is.close();
			httpURLConnection.disconnect();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// BitmapFactory.decodeFile(filePath, options);
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 720, 1280);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
