package org.activiti.extension.schedule;

import org.activiti.biz.extension.account.ExtensionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被Spring各种Scheduler反射调用的Service POJO
 * 
 * @author chenlg
 */
@Component
public class UserCountScanner {

	private static Logger logger = LoggerFactory.getLogger(UserCountScanner.class);

	@Autowired
	private ExtensionService extensionService;

	public void executeByJdk() {
		execute("jdk timer job");
	}

	public void executeBySpringCronByJava() {
		execute("spring cron job by java");
	}

	//被Spring的Quartz MethodInvokingJobDetailFactoryBean反射执行
	public void executeByQuartzLocalJob() {
		execute("quartz local job");
	}

	//被Spring的Scheduler namespace 反射构造成ScheduledMethodRunnable
	public void executeBySpringCronByXml() {
		execute("spring cron job by xml");
	}

	//被Spring的Scheduler namespace 反射构造成ScheduledMethodRunnable
	public void executeBySpringTimerByXml() {
		execute("spring timer job by xml");
	}

	/**
	 * 定时打印当前用户数到日志.
	 */
	private void execute(String by) {
		long userCount = extensionService.getUserCount();
		logger.info("There are {} user in database, printed by {}.", userCount, by);
	}
}
