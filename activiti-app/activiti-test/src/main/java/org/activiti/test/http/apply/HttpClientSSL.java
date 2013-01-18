package org.activiti.test.http.apply;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.activiti.test.http.DefaultRedirectHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public class HttpClientSSL extends HttpCookieContext {
	private static DefaultHttpClient httpclient = null;

	public static HttpEntity HttpGet(String urlHost) {

		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, null);
		HttpGet httpget = new HttpGet(urlHost);

		HttpResponse response;
		try {
			response = httpclient.execute(httpget, localContext);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static HttpEntity HttpPost(String urlHost,
			List<NameValuePair> formparams) {

		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, null);
		HttpPost httppost = new HttpPost(urlHost);

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			HttpResponse response;

			response = httpclient.execute(httppost, localContext);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static DefaultHttpClient createHttpClient() {
		if (httpclient != null)
			return httpclient;
		else
			httpclient = new DefaultHttpClient();

		try {
			TrustManager easyTrustManager = new X509TrustManager() {
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] x509Certificates,
						String s)
						throws java.security.cert.CertificateException {
					// To change body of implemented methods use File | Settings
					// | File Templates.
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] x509Certificates,
						String s)
						throws java.security.cert.CertificateException {
					// To change body of implemented methods use File | Settings
					// | File Templates.
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[0]; // To
																		// change
																		// body
																		// of
																		// implemented
																		// methods
																		// use
																		// File
																		// |
																		// Settings
																		// |
																		// File
																		// Templates.
				}
			};

			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext
					.init(null, new TrustManager[] { easyTrustManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			Scheme sch = new Scheme("https", sf, 443);

			httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(final HttpRequest request,
					final HttpContext context) throws HttpException,
					IOException {
				// if (!request.containsHeader("Accept-Encoding")) {
				// request.addHeader("Accept-Encoding", "gzip");
				// }
				if (!request.containsHeader("Accept")) {
					request.addHeader("Accept", "*/*");
				}
				if (request.containsHeader("User-Agent")) {
					request.removeHeaders("User-Agent");
				}
				if (request.containsHeader("Connection")) {
					request.removeHeaders("Connection");
				}
				request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:8.0) Gecko/20100101 Firefox/8.0");
				request.addHeader("Connection", "keep-alive");
				request.addHeader("Referer", "https://dynamic.12306.cn/otsweb/");
				// request.addHeader("Connection", "close");
			}

		});
		httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(final HttpResponse response,
					final HttpContext context) throws HttpException,
					IOException {
 				HttpEntity entity = response.getEntity();
			}
		});
		httpclient.setRedirectHandler(new DefaultRedirectHandler());
		
		return httpclient;
	}

}
