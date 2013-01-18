package org.activiti.test.http.apply;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

public class HttpCookieStore {

	/**
	 * 根据url 获取cookie
	 * @param urlHost
	 * @param cookieStr
	 * @return
	 */
	public static CookieStore createCookieStore(String urlHost, String cookieStr) {
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		if (cookieStr == null || "".equals(cookieStr))
			return cookieStore;
		String domain = urlHost.substring(urlHost.indexOf("//")+2);
		if (null != cookieStr && !cookieStr.trim().equals("")) {
			String[] cookies = cookieStr.split(";");
			// userCookieList = new AttributeList();
			for (int i = 0; i < cookies.length; i++) {
				int _i = cookies[i].indexOf("=");
				if (_i != -1) {
					String name = cookies[i].substring(0, _i);
					String value = cookies[i].substring(_i + 1);
					BasicClientCookie _cookie = new BasicClientCookie(name,
							value);
					_cookie.setDomain(domain);
					cookieStore.addCookie(_cookie);
				}
			}
		}
		return cookieStore;
	}
}
