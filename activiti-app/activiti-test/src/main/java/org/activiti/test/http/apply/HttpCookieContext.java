package org.activiti.test.http.apply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpCookieContext extends HttpCookieStore {
	private static Map<String, HttpContext> _threadHttpContext = new HashMap<String, HttpContext>();

	public static HttpContext getHttpContext(String urlHost, String cookies) {
		HttpContext httpContext = _threadHttpContext.get("httpContext");

		if (httpContext != null) {
			printCookies(httpContext);
			return httpContext;
		}
		httpContext = new BasicHttpContext();
		CookieStore cookieStore = createCookieStore(urlHost, cookies);
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		_threadHttpContext.put("httpContext", httpContext);
		printCookies(httpContext);
		return httpContext;
	}

	/**
	 * 打印Cookies信息
	 * 
	 * @param httpContext
	 */
	public static void printCookies(HttpContext httpContext) {
//		CookieStore cookieStore = (CookieStore) httpContext
//				.getAttribute(ClientContext.COOKIE_STORE);
//		List<Cookie> cookies = cookieStore.getCookies();
//		if (cookies.isEmpty()) {
//			System.out.println("None");
//		} else {
//			for (int i = 0; i < cookies.size(); i++) {
//				System.out.println("- " + cookies.get(i).toString());
//			}
//		}
	}
}
