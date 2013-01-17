package org.activiti.test.function;

import java.net.URL;
import java.sql.Driver;

import org.activiti.test.data.DataFixtures;
import org.activiti.test.jetty.JettyFactory;
import org.activiti.utils.extension.loader.PropertiesLoader;
import org.eclipse.jetty.server.Server;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * 功能测试基类.
 * 
 * 在整个测试期间启动一次Jetty Server, 并在每个TestCase Class执行前中重新载入默认数据.
 * 
 * @author calvin
 */
public class BaseFunctionalTestCase {
	protected static String baseUrl;

	protected static Server jettyServer;

	protected static SimpleDriverDataSource dataSource;

	protected static PropertiesLoader propertiesLoader = new PropertiesLoader("classpath:/application.properties");

	private static Logger logger = LoggerFactory.getLogger(BaseFunctionalTestCase.class);

	@BeforeClass
	public static void beforeClass() throws Exception {
		baseUrl = propertiesLoader.getProperty("baseUrl", ShowcaseServer.BASE_URL);

		Boolean isEmbedded = new URL(baseUrl).getHost().equals("localhost");

		if (isEmbedded) {
			startJettyOnce();
		}

		buildDataSourceOnce();
		reloadSampleData();
	}

	/**
	 * 启动Jetty服务器, 仅启动一次.
	 */
	protected static void startJettyOnce() throws Exception {
		if (jettyServer == null) {
			//设定Spring的profile
			System.setProperty("spring.profiles.active", "functional");

			jettyServer = JettyFactory.createServerInSource(new URL(baseUrl).getPort(), ShowcaseServer.CONTEXT);
			JettyFactory.setTldJarNames(jettyServer, ShowcaseServer.TLD_JAR_NAMES);
			jettyServer.start();

			logger.info("Jetty Server started at " + baseUrl);
		}
	}

	/**
	 * 构造数据源，仅构造一次.
	 */
	protected static void buildDataSourceOnce() throws ClassNotFoundException {
		if (dataSource == null) {
			dataSource = new SimpleDriverDataSource();
			dataSource.setDriverClass((Class<? extends Driver>) Class.forName(propertiesLoader
					.getProperty("jdbc.driver")));
			dataSource.setUrl(propertiesLoader.getProperty("jdbc.url"));
			dataSource.setUsername(propertiesLoader.getProperty("jdbc.username"));
			dataSource.setPassword(propertiesLoader.getProperty("jdbc.password"));

		}
	}

	/**
	 * 载入默认数据.
	 */
	protected static void reloadSampleData() throws Exception {
		DataFixtures.executeScript(dataSource, "classpath:data/cleanup-data.sql", "classpath:data/import-data.sql");
	}

}
