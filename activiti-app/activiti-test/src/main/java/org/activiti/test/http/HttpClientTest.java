package org.activiti.test.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void getHttp() throws ParseException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet("http://localhost:8080/activiti-webapp/story/webservice");
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			HttpResponse response = httpclient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			System.out.println("--------------------------------------");
			// 打印响应状态
			System.out.println(response.getStatusLine());
			if (entity != null) {
				// 打印响应内容长度
				System.out.println("Response content length: "
						+ entity.getContentLength());
				// 打印响应内容
				System.out.println("Response content: "
						+ EntityUtils.toString(entity));
			}
			System.out.println("------------------------------------");
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
	}

	@Test
	public void postHttp() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://localhost:8080/myDemo/Ajax/serivceJ.action");
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response;
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("--------------------------------------");
				System.out.println("Response content: "
						+ EntityUtils.toString(entity, "UTF-8"));
				System.out.println("--------------------------------------");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
	}

	@Test
	public void loginHttp() {
		CookieStore cookiestore = null;
		// 创建默认的httpClient实例.
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// 创建httppost
		try {
			HttpPost httppost = new HttpPost(
					"http://localhost:8080/activiti-webapp/login");
			// 创建参数队列
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("username", "admin"));
			formparams.add(new BasicNameValuePair("password", "admin"));
			UrlEncodedFormEntity uefEntity;

			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response;
			response = httpclient.execute(httppost);
			cookiestore=httpclient.getCookieStore();
			// 打印响应状态
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("--------------------------------------");
				System.out.println("Response content: "
						+ EntityUtils.toString(entity, "UTF-8"));
				System.out.println("--------------------------------------");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}

		DefaultHttpClient httpclient2 = new DefaultHttpClient();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet("http://localhost:8080/activiti-webapp/story/webservice");
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			httpclient2.setCookieStore(cookiestore);
			HttpResponse response = httpclient2.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			System.out.println("--------------------------------------");
			// 打印响应状态
			System.out.println(response.getStatusLine());
			if (entity != null) {
				// 打印响应内容长度
				System.out.println("Response content length: "
						+ entity.getContentLength());
				// 打印响应内容
				System.out.println("Response content: "
						+ EntityUtils.toString(entity));
			}
			System.out.println("------------------------------------");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// 关闭连接,释放资源
			httpclient2.getConnectionManager().shutdown();
		}

	}

}
