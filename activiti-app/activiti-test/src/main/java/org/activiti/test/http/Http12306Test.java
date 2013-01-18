package org.activiti.test.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.test.http.apply.HttpClientSSL;
import org.activiti.test.http.apply.HttpVerificationCode;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import net.sf.json.JSONObject;


public class Http12306Test {
	@Test
	public void loginPost() throws Exception {
		//
		List<NameValuePair> formparams1 = new ArrayList<NameValuePair>();
		HttpEntity entity1 = HttpClientSSL
				.HttpPost("https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest",formparams1);
		if (entity1 != null) {
			System.out.println("--------------------------------------");
			String result = EntityUtils.toString(entity1, "UTF-8");
			System.out.println("Response content: " + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String res = jsonObject.getString("loginRand");
			System.out.println(res);
		}
	}
	@Test
	public void login() throws Exception {
		List<NameValuePair> formparams1 = new ArrayList<NameValuePair>();
		HttpEntity entity1 = HttpClientSSL
				.HttpPost("https://dynamic.12306.cn/otsweb/loginAction.do?method=loginAysnSuggest",formparams1);
		String loginRand = "";
		if (entity1 != null) {
			System.out.println("--------------------------------------");
			String result = EntityUtils.toString(entity1, "UTF-8");
			System.out.println("Response content: " + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			loginRand = jsonObject.getString("loginRand");
			
		}
		System.out.println(loginRand);

		System.out.println("--正在登录--");

		String body = "请输入正确的验证码";
		// do{
		String code = "";
		if (body.indexOf("请输入正确的验证码") != -1) {

			File file = HttpVerificationCode
					.doGetFile("http://www.12306.cn/otsweb/passCodeAction.do?rand=sjrand");
			// http://dynamic.12306.cn/TrainQuery/passCodeActi0n.do?rand=rrand?0.5886299789417535
			File codeFile = new File(TrainDataUtils.saveLoginCodePath);
			if (!codeFile.exists())
				codeFile.createNewFile();
			FileUtils.copyFile(file, codeFile);
			code = readString("请输入登录验证码");

		}
		System.out.println("--发送登录请求--" + code);

		
		// // 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		formparams.add(new BasicNameValuePair("loginRand", loginRand));
		formparams.add(new BasicNameValuePair("refundLogin", "N"));
		formparams.add(new BasicNameValuePair("refundFlag", "Y"));
		formparams.add(new BasicNameValuePair("nameErrorFocus", ""));
		formparams.add(new BasicNameValuePair("passwordErrorFocus", ""));
		formparams.add(new BasicNameValuePair("randErrorFocus", ""));
		formparams.add(new BasicNameValuePair("loginUser.user_name",TrainDataUtils.username));
		formparams.add(new BasicNameValuePair("user.password",TrainDataUtils.password));
		formparams.add(new BasicNameValuePair("randCode", code));
//		loginRand=453&refundLogin=N&refundFlag=Y&loginUser.user_name=alvinchen9
//				&nameErrorFocus=&user.password=elingfen9&passwordErrorFocus=
//				&randCode=5b8a&randErrorFocus=
		// 创建参数队列
		// List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		//
		// formparams.add(new BasicNameValuePair("nyear1", "2013"));
		// formparams.add(new BasicNameValuePair("nyear1_new_value", "false"));
		// formparams.add(new BasicNameValuePair("nmonth1", "01"));
		// formparams.add(new BasicNameValuePair("nmonth1_new_value", "false"));
		// formparams.add(new BasicNameValuePair("nday1", "18"));
		// formparams.add(new BasicNameValuePair("nday1_new_value", "false"));
		// formparams.add(new BasicNameValuePair("trainCode1","D3101"));
		// formparams.add(new BasicNameValuePair("trainCode1_new_value",
		// "true"));
		// formparams.add(new BasicNameValuePair("randCode", code));
		
		try {//
				// http://dynamic.12306.cn/TrainQuery/iframeTrainPassStationByTrainCode.jsp
			HttpEntity entity = HttpClientSSL
					.HttpPost(
							"https://dynamic.12306.cn/otsweb/loginAction.do?method=login",
							formparams);
			if (entity != null) {
				System.out.println("--------------------------------------");
				String result = EntityUtils.toString(entity, "UTF-8");
				System.out.println("Response content: " + result);

				if (result.indexOf("请输入正确的验证码") != -1) {
					System.out.println("请输入正确的验证码");

				} else if (result.indexOf("当前访问用户过多，请稍后重试") != -1) {
					System.out.println("当前访问用户过多，请稍后重试");

				} else if (result.indexOf("您最后一次登录时间为") != -1) {
					System.out.println("您最后一次登录时间为");

				}

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
		//	httpclient.getConnectionManager().shutdown();
		}

	}

	/**
	 * 多控制台读取验证码
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private static String readString(String msg) throws Exception {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			System.out.print(msg + ": ");
			return bufferedReader.readLine();
		} catch (Exception e) {
		}
		return "1245";
	}

	@Test
	public void getHttp1() {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(
					"https://dynamic.12306.cn/otsweb/login.jsp");
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
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}

	}

}
