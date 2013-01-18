/**
 * 
 */
package org.activiti.test.http.apply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

/**
 * @author chenlg
 * 
 */
public class HttpVerificationCode extends HttpClientSSL{
	private static String tempfilepath = "C:/";
	
	public static File doGetFile(String url) {
		try {
			 
			HttpEntity entity = HttpClientSSL.HttpGet(url);
			String filename = new Date().getTime() + ".temp";
			if (entity != null) {
				BufferedInputStream bis = new BufferedInputStream(
						entity.getContent());
				File file = new File(tempfilepath + "/" + filename);
				FileOutputStream fs = new FileOutputStream(file);

				byte[] buf = new byte[1024];
				int len = bis.read(buf);
				if (len == -1 || len == 0) {
					file.delete();
					file = null;
					entity.consumeContent();
					return file;
				}
				while (len != -1) {
					fs.write(buf, 0, len);
					len = bis.read(buf);
				}
				fs.close();

				entity.consumeContent();
				return file;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
